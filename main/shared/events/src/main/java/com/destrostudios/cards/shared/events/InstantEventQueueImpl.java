package com.destrostudios.cards.shared.events;

public class InstantEventQueueImpl implements EventQueue {

    public InstantEventQueueImpl(IterableEventQueue iterableEventQueue) {
        this.iterableEventQueue = iterableEventQueue;
    }
    private IterableEventQueue iterableEventQueue;

    @Override
    public void fireActionEvent(Event event) {
        iterableEventQueue.fireActionEvent(event);
        while (iterableEventQueue.processNextEvent()) {
        }
    }

    @Override
    public void fireChainEvent(Event event) {
        iterableEventQueue.fireChainEvent(event);
    }

    @Override
    public void fireSubEvent(Event event) {
        iterableEventQueue.fireSubEvent(event);
    }
}
