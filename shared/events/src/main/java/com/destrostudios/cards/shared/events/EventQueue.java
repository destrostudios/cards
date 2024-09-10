package com.destrostudios.cards.shared.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

public class EventQueue<C> {

    private static final Logger LOG = LoggerFactory.getLogger(EventQueue.class);

    public EventQueue(EventHandling<C> handling) {
        this.handling = handling;
        pendingEventHandlers = new LinkedList<>();
    }
    private EventHandling<C> handling;
    private Event parentEvent;
    // LinkedList performs better than ArrayList here
    private LinkedList<PendingEventHandler<Event, C>> pendingEventHandlers;

    public void fire(Event event) {
        event.setParent(parentEvent);
        Event root = event.getRoot();
        triggerHandlers(handling.pre(), event, (otherEvent) -> otherEvent.getParent() != parentEvent);
        triggerHandlers(handling.instant(), event, (otherEvent) -> otherEvent.getParent() != parentEvent);
        triggerHandlers(handling.resolved(), event, (otherEvent) -> otherEvent.getRoot() != root);
    }

    private <E extends Event> void triggerHandlers(EventHandlers<C> eventHandlers, E event, Predicate<Event> stopAndInsertEventHandlers) {
        EventHandler<E, C>[] handlers = eventHandlers.get(event);
        if (handlers != null) {
            int startingIndex = 0;
            for (PendingEventHandler<Event, C> pendingEventHandler : pendingEventHandlers) {
                if (stopAndInsertEventHandlers.test(pendingEventHandler.event())) {
                    break;
                }
                startingIndex++;
            }
            for (int i = 0; i < handlers.length; i++) {
                pendingEventHandlers.add(startingIndex + i, new PendingEventHandler(event, handlers[i]));
            }
        }
    }

    public boolean hasPendingEventHandler() {
        return (pendingEventHandlers.size() > 0);
    }

    public PendingEventHandler getNextPendingEventHandler() {
        return pendingEventHandlers.getFirst();
    }

    public void triggerNextEventHandler(C context) {
        PendingEventHandler<Event, C> pendingEventHandler = pendingEventHandlers.poll();
        Event event = pendingEventHandler.event();
        LOG.trace("Handling {}", event);
        parentEvent = event;
        pendingEventHandler.handler().handle(context, event);
        parentEvent = null;
        if (event.isCancelled()) {
            removeCancelledHandlers();
        }
    }

    private void removeCancelledHandlers() {
        for (int i = 0; i < pendingEventHandlers.size(); i++) {
            PendingEventHandler<Event, C> pendingPendingEventHandler = pendingEventHandlers.get(i);
            if (pendingPendingEventHandler.event().isSomeParentCancelled()) {
                LOG.trace("{} was cancelled", pendingPendingEventHandler);
                pendingEventHandlers.remove(i);
                i--;
            }
        }
    }

    public void clear() {
        pendingEventHandlers.clear();
    }
}
