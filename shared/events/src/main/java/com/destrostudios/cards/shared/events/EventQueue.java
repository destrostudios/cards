package com.destrostudios.cards.shared.events;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

public class EventQueue {

    private static final Logger LOG = LoggerFactory.getLogger(EventQueue.class);

    private EventHandlers preHandlers = new EventHandlers();
    private EventHandlers instantHandlers = new EventHandlers();
    private EventHandlers resolvedHandlers = new EventHandlers();
    private Event parentEvent;
    private LinkedList<PendingEventHandler> pendingEventHandlers = new LinkedList<>();

    public void fire(Event event, NetworkRandom random) {
        event.setParent(parentEvent);
        Event root = event.getRoot();
        triggerHandlers(preHandlers, event, random, (otherEvent) -> otherEvent.getParent() != parentEvent);
        triggerHandlers(instantHandlers, event, random, (otherEvent) -> otherEvent.getParent() != parentEvent);
        triggerHandlers(resolvedHandlers, event, random, (otherEvent) -> otherEvent.getRoot() != root);
    }

    private <T extends Event> void triggerHandlers(EventHandlers eventHandlers, T event, NetworkRandom random, Predicate<Event> stopAndInsertEventHandlers) {
        EventHandler[] handlers = eventHandlers.get(event);
        if (handlers != null) {
            int startingIndex = 0;
            for (PendingEventHandler pendingEventHandler : pendingEventHandlers) {
                if (stopAndInsertEventHandlers.test(pendingEventHandler.getEvent())) {
                    break;
                }
                startingIndex++;
            }
            for (int i = 0; i < handlers.length; i++) {
                pendingEventHandlers.add(startingIndex + i, new PendingEventHandler(event, handlers[i], random));
            }
        }
    }

    public boolean hasPendingEventHandler() {
        return (pendingEventHandlers.size() > 0);
    }

    public void triggerNextEventHandler() {
        PendingEventHandler pendingEventHandler = pendingEventHandlers.poll();
        Event event = pendingEventHandler.getEvent();
        LOG.trace("Handling {}", event);
        parentEvent = event;
        pendingEventHandler.handleEvent();
        parentEvent = null;
        if (event.isCancelled()) {
            removeCancelledHandlers();
        }
    }

    private void removeCancelledHandlers() {
        for (int i = 0; i < pendingEventHandlers.size(); i++) {
            PendingEventHandler pendingPendingEventHandler = pendingEventHandlers.get(i);
            if (pendingPendingEventHandler.getEvent().isSomeParentCancelled()) {
                LOG.trace("{} was cancelled", pendingPendingEventHandler);
                pendingEventHandlers.remove(i);
                i--;
            }
        }
    }

    public EventHandlers pre() {
        return preHandlers;
    }

    public EventHandlers instant() {
        return instantHandlers;
    }

    public EventHandlers resolved() {
        return resolvedHandlers;
    }
}
