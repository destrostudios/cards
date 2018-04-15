package com.etherblood.cards.events;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class EventQueueImpl implements EventQueue {

    private static final int SUCCESSIVE_EVENTS_LIMIT = 1000;
    private static final int ROOT_QUEUE = 0;

    private final List<Queue<Event>> eventStack = new ArrayList<>();
    private final Consumer<Event> eventConsumer;
    private int depth = 0;
    private int successiveEventsCount;
    private Event activeEvent = null;

    public EventQueueImpl(Consumer<Event> eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @Override
    public void action(ActionEvent action) {
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
            eventConsumer.accept(activeEvent);
            if (activeEvent.isCancelled()) {
                getQueue(depth).clear();
            } else {
                processEvents();
            }
        }
        depth--;
    }

    @Override
    public void trigger(TriggeredEvent event) {
        assert depth > 0;
        enqueue(getQueue(depth - 1), event);
    }

    @Override
    public void response(ResponseEvent event) {
        assert depth > 0;
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
