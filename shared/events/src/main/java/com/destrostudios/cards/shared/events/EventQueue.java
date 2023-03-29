package com.destrostudios.cards.shared.events;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EventQueue {

    private static final Logger LOG = LoggerFactory.getLogger(EventQueue.class);

    private final EventHandlers preHandlers = new EventHandlers();
    private final EventHandlers instantHandlers = new EventHandlers();
    private final EventHandlers resolvedHandlers = new EventHandlers();
    private Event parentEvent;
    private LinkedList<PendingEventHandler> pendingEventHandlers = new LinkedList<>();
    private Map<Event, List<PendingEventHandler>> waitingForRootResolvedEventHandlers = new HashMap<>();

    public void fire(Event event, NetworkRandom random) {
        event.setParent(parentEvent);
        triggerHandlers(preHandlers, event, random);
        triggerHandlers(instantHandlers, event, random);
        Event rootEvent = event.getRoot();
        for (EventHandler eventHandler : resolvedHandlers.get(event.getClass())) {
            waitingForRootResolvedEventHandlers.computeIfAbsent(rootEvent, e -> new LinkedList<>()).add(new PendingEventHandler(event, eventHandler, random));
        }
    }

    // TODO: Will be used for cases like Defile, where subtrees should be resolved (e.g. deathrattles between the different aoes)
    public void fireAndResolve(Event event, NetworkRandom random) {
        triggerHandlers(preHandlers, event, random);
        triggerHandlers(instantHandlers, event, random);
        triggerHandlers(resolvedHandlers, event, random);
    }

    private <T extends Event> void triggerHandlers(EventHandlers eventHandlers, T event, NetworkRandom random) {
        Iterator<EventHandler> eventHandlersIterator = eventHandlers.get(event.getClass()).iterator();
        int startingIndex = 0;
        for (PendingEventHandler pendingEventHandler : pendingEventHandlers) {
            if (pendingEventHandler.getEvent().getParent() != event.getParent()) {
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
        // Add first and then remove cancelled ones (In case any of those new handlers were cancelled, they are being removed directly as well)
        addHandlersWaitingForRootResolved(event.getRoot());
        removeCancelledHandlers();
    }

    private void addHandlersWaitingForRootResolved(Event rootEvent) {
        boolean isRootResolved = pendingEventHandlers.stream().noneMatch(pendingTriggeredHandler -> pendingTriggeredHandler.getEvent().getRoot() == rootEvent);
        if (isRootResolved) {
            List<PendingEventHandler> resolvedEventHandlers = waitingForRootResolvedEventHandlers.remove(rootEvent);
            if (resolvedEventHandlers != null) {
                pendingEventHandlers.addAll(resolvedEventHandlers);
            }
        }
    }

    private void removeCancelledHandlers() {
        for (int i = 0; i < pendingEventHandlers.size(); i++) {
            PendingEventHandler pendingPendingEventHandler = pendingEventHandlers.get(i);
            if (pendingPendingEventHandler.getEvent().isCancelled()) {
                LOG.debug("{} was cancelled", pendingPendingEventHandler);
                pendingEventHandlers.remove(i);
                waitingForRootResolvedEventHandlers.remove(pendingPendingEventHandler.getEvent());
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
