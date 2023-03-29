package com.destrostudios.cards.shared.events;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiPredicate;

public class EventQueue {

    private static final Logger LOG = LoggerFactory.getLogger(EventQueue.class);

    private EventHandlers preHandlers = new EventHandlers();
    private EventHandlers instantHandlers = new EventHandlers();
    private EventHandlers resolvedHandlers = new EventHandlers();
    private Event parentEvent;
    private LinkedList<PendingEventHandler> pendingEventHandlers = new LinkedList<>();

    public void fire(Event event, NetworkRandom random) {
        event.setParent(parentEvent);
        triggerHandlers(preHandlers, event, random, (event1, event2) -> event1.getParent() != event2.getParent());
        triggerHandlers(instantHandlers, event, random, (event1, event2) -> event1.getParent() != event2.getParent());
        triggerHandlers(resolvedHandlers, event, random, (event1, event2) -> event1.getRoot() != event2.getRoot());
    }

    private <T extends Event> void triggerHandlers(EventHandlers eventHandlers, T event, NetworkRandom random, BiPredicate<Event, Event> stopAndInsertEventHandlers) {
        Iterator<EventHandler> eventHandlersIterator = eventHandlers.get(event.getClass()).iterator();
        int startingIndex = 0;
        for (PendingEventHandler pendingEventHandler : pendingEventHandlers) {
            if (stopAndInsertEventHandlers.test(pendingEventHandler.getEvent(), event)) {
                break;
            }
            startingIndex++;
        }
        int i = startingIndex;
        while (eventHandlersIterator.hasNext()) {
            EventHandler eventHandler = eventHandlersIterator.next();
            pendingEventHandlers.add(i, new PendingEventHandler(event, eventHandler, random));
            i++;
        }
    }

    public boolean hasPendingEventHandler() {
        return (pendingEventHandlers.size() > 0);
    }

    public void triggerNextEventHandler() {
        PendingEventHandler pendingEventHandler = pendingEventHandlers.poll();
        Event event = pendingEventHandler.getEvent();
        parentEvent = event;
        LOG.debug("Handling {}", event);
        pendingEventHandler.handleEvent();
        parentEvent = null;
        removeCancelledHandlers();
    }

    private void removeCancelledHandlers() {
        for (int i = 0; i < pendingEventHandlers.size(); i++) {
            PendingEventHandler pendingPendingEventHandler = pendingEventHandlers.get(i);
            if (pendingPendingEventHandler.getEvent().isCancelled()) {
                LOG.debug("{} was cancelled", pendingPendingEventHandler);
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
