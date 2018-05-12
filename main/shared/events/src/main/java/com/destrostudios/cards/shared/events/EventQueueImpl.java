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
public class EventQueueImpl implements EventQueue {

    private static final Logger LOG = LoggerFactory.getLogger(EventQueueImpl.class);
    private static final int SUCCESSIVE_EVENTS_LIMIT = 1000;
    private static final int ROOT_QUEUE = 0;

    private final List<Queue<Event>> eventStack = new ArrayList<>();
    private final Consumer<Event> eventConsumer, preConsumer, postConsumer;
    private int depth = 0;
    private int successiveEventsCount;
    private Event activeEvent = null;

    public EventQueueImpl(Consumer<Event> eventConsumer) {
        this(eventConsumer, x -> {}, x -> {});
    }

    public EventQueueImpl(Consumer<Event> eventConsumer, Consumer<Event> preConsumer, Consumer<Event> postConsumer) {
        this.eventConsumer = eventConsumer;
        this.preConsumer = preConsumer;
        this.postConsumer = postConsumer;
    }

    @Override
    public void fireActionEvent(Event action) {
        if (depth != 0) {
            throw new IllegalStateException("actions may not be enqueued during event handling");
        }
        enqueue(getQueue(ROOT_QUEUE), action);
        successiveEventsCount = 0;
        processEvents();
    }
    
    private void processEvents() {
        Queue<Event> queue = getQueue(depth);
        depth++;
        while ((activeEvent = queue.poll()) != null) {
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
            } else {
                Event processedEvent = activeEvent;
                processEvents();
                postConsumer.accept(processedEvent);
            }
        }
        depth--;
    }

    @Override
    public void fireChainEvent(Event event) {
        enqueue(getQueue(depth - 1), event);
    }

    @Override
    public void fireSubEvent(Event event) {
        enqueue(getQueue(depth), event);
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

}
