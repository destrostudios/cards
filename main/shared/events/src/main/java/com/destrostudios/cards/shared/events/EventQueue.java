package com.destrostudios.cards.shared.events;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SuppressWarnings({"unchecked"})
public class EventQueue {

    private static final Logger LOG = LoggerFactory.getLogger(EventQueue.class);

    private final EventHandlers preHandlers = new EventHandlers();
    private final EventHandlers instantHandlers = new EventHandlers();
    private final EventHandlers resolvedHandlers = new EventHandlers();
    private Event parentEvent;
    private LinkedList<TriggeredEventHandler> triggeredEventHandlers = new LinkedList<>();
    private Map<Event, List<TriggeredEventHandler>> waitingResolvedEventHandlers = new HashMap<>();

    public void fire(Event event, NetworkRandom random) {
        event.setParent(parentEvent);
        triggerHandlers(preHandlers, event, random);
        triggerHandlers(instantHandlers, event, random);
        Event rootEvent = event.getRoot();
        for (EventHandler eventHandler : resolvedHandlers.get(event.getClass())) {
            waitingResolvedEventHandlers.computeIfAbsent(rootEvent, e -> new LinkedList<>()).add(new TriggeredEventHandler(event, eventHandler, random));
        }
    }

    private <T extends Event> void triggerHandlers(EventHandlers eventHandlers, T event, NetworkRandom random) {
        Iterator<EventHandler> eventHandlersIterator = eventHandlers.get(event.getClass()).iterator();
        int startingIndex = 0;
        for (TriggeredEventHandler tiggeredEventHandler : triggeredEventHandlers) {
            if (tiggeredEventHandler.getEvent().getParent() != event.getParent()) {
                break;
            }
            startingIndex++;
        }
        int i = startingIndex;
        while (eventHandlersIterator.hasNext()) {
            EventHandler eventHandler = eventHandlersIterator.next();
            triggeredEventHandlers.add(i, new TriggeredEventHandler(event, eventHandler, random));
            i++;
        }
    }

    public boolean hasNextTriggeredHandler() {
        return (triggeredEventHandlers.size() > 0);
    }

    public void triggerNextHandler() {
        TriggeredEventHandler triggeredHandler = triggeredEventHandlers.poll();
        Event event = triggeredHandler.getEvent();
        parentEvent = event;
        LOG.debug("handling {}", event);
        triggeredHandler.handleEvent();
        parentEvent = null;
        checkCancelledEvents();
        checkIfRootEventIsResolved(event.getRoot());
    }

    private void checkCancelledEvents() {
        for (int i = 0; i < triggeredEventHandlers.size(); i++) {
            TriggeredEventHandler pendingTriggeredEventHandler = triggeredEventHandlers.get(i);
            if (pendingTriggeredEventHandler.getEvent().isCancelled()) {
                LOG.debug("{} was cancelled", pendingTriggeredEventHandler);
                triggeredEventHandlers.remove(i);
                i--;
            }
        }
    }

    private void checkIfRootEventIsResolved(Event rootEvent) {
        boolean isRootResolved = true;
        for (TriggeredEventHandler pendingTriggeredHandler : triggeredEventHandlers) {
            Event pendingRootEvent = pendingTriggeredHandler.getEvent().getRoot();
            if (pendingRootEvent == rootEvent) {
                isRootResolved = false;
                break;
            }
        }
        if (isRootResolved) {
            List<TriggeredEventHandler> resolvedEventHandlers = waitingResolvedEventHandlers.remove(rootEvent);
            if (resolvedEventHandlers != null) {
                triggeredEventHandlers.addAll(resolvedEventHandlers);
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
