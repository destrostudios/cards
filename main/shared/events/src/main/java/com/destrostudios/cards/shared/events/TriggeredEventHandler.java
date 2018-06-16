package com.destrostudios.cards.shared.events;

@SuppressWarnings({"unchecked"})
public class TriggeredEventHandler {

    TriggeredEventHandler(Event event, EventHandler eventHandler) {
        this.event = event;
        this.eventHandler = eventHandler;
    }
    private Event event;
    private EventHandler eventHandler;

    public void handleEvent() {
        eventHandler.onEvent(event);
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "[TriggeredEventHandler event=" + event + " eventHandler=" + eventHandler + "]";
    }
}
