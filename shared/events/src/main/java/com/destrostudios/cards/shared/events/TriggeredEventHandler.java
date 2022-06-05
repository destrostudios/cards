package com.destrostudios.cards.shared.events;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

@SuppressWarnings({"unchecked"})
public class TriggeredEventHandler {

    TriggeredEventHandler(Event event, EventHandler eventHandler, NetworkRandom random) {
        this.event = event;
        this.eventHandler = eventHandler;
        this.random = random;
    }
    private Event event;
    private EventHandler eventHandler;
    private NetworkRandom random;

    public void handleEvent() {
        eventHandler.onEvent(event, random);
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "[TriggeredEventHandler event=" + event + " eventHandler=" + eventHandler + "]";
    }
}
