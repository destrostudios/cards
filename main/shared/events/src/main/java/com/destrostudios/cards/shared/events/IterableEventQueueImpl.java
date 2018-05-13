package com.destrostudios.cards.shared.events;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class IterableEventQueueImpl implements IterableEventQueue {

    private static final Logger LOG = LoggerFactory.getLogger(IterableEventQueueImpl.class);
    private static final int SUCCESSIVE_EVENTS_LIMIT = 1000;

    private final List<Queue<Event>> eventStack = new ArrayList<>();
    private final Consumer<Event> eventConsumer, preConsumer, postConsumer;
    private int depth = 0;
    private int successiveEventsCount;
    private Event initialEvent = null;
    private Event activeEvent = null;

    public IterableEventQueueImpl(Consumer<Event> preConsumer, Consumer<Event> eventConsumer, Consumer<Event> postConsumer) {
        this.preConsumer = preConsumer;
        this.eventConsumer = eventConsumer;
        this.postConsumer = postConsumer;
    }

    @Override
    public void fireActionEvent(Event action) {
        if (hasNext()) {
            throw new IllegalStateException("actions may not be enqueued during event handling");
        }
        enqueue(getQueue(0), action);
        successiveEventsCount = 0;
        initialEvent = action;
    }

    @Override
    public void fireChainEvent(Event event) {
        enqueue(getQueue(depth), event);
    }

    @Override
    public void fireSubEvent(Event event) {
        enqueue(getQueue(depth + 1), event);
    }

    private void enqueue(Queue<Event> queue, Event event) {
        event.setParent(activeEvent);
        queue.offer(event);
    }

    private Queue<Event> getQueue(int depth) {
        while (eventStack.size() <= depth) {
            eventStack.add(new ArrayDeque<>());
        }
        return eventStack.get(depth);
    }

    @Override
    public void processNextEvent() {
        depth++;
        Queue<Event> queue = getQueue(depth);
        if (queue.isEmpty()) {
            if (depth > 0) {
                depth -= 2;
                if (activeEvent != null) {
                    postConsumer.accept(activeEvent);
                    activeEvent = null;
                }
            }
            else {
                if (initialEvent != null) {
                    postConsumer.accept(initialEvent);
                    initialEvent = null;
                }
                activeEvent = null;
            }
            return;
        }
        activeEvent = queue.poll();
        successiveEventsCount++;
        if (successiveEventsCount > SUCCESSIVE_EVENTS_LIMIT) {
            throw new IllegalStateException("successive events limit reached");
        }
        LOG.debug("handling {}", activeEvent);
        preConsumer.accept(activeEvent);
        eventConsumer.accept(activeEvent);
        if (activeEvent.isCancelled()) {
            LOG.debug("{} was cancelled", activeEvent);
            getQueue(depth).clear();
        }
    }

    @Override
    public boolean hasNext() {
        return (initialEvent != null);
    }
}
