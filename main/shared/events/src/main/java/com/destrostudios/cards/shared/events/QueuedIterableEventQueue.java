package com.destrostudios.cards.shared.events;

import java.util.LinkedList;
import java.util.Queue;

public class QueuedIterableEventQueue implements IterableEventQueue {

    public QueuedIterableEventQueue(IterableEventQueue iterableEventQueue) {
        this.iterableEventQueue = iterableEventQueue;
    }
    private IterableEventQueue iterableEventQueue;
    private Queue<Event> actionEventsQueue = new LinkedList<>();

    @Override
    public void fireActionEvent(Event action) {
        actionEventsQueue.add(action);
    }

    @Override
    public void fireChainEvent(Event event) {
        iterableEventQueue.fireChainEvent(event);
    }

    @Override
    public void fireSubEvent(Event event) {
        iterableEventQueue.fireSubEvent(event);
    }

    @Override
    public void processNextEvent() {
        if (iterableEventQueue.hasNext()) {
            iterableEventQueue.processNextEvent();
        }
        else {
            Event actionEvent = actionEventsQueue.poll();
            if (actionEvent != null) {
                iterableEventQueue.fireActionEvent(actionEvent);
                iterableEventQueue.processNextEvent();
            }
        }
    }

    @Override
    public boolean hasNext() {
        return (iterableEventQueue.hasNext() || (!actionEventsQueue.isEmpty()));
    }
}
