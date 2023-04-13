package com.destrostudios.cards.shared.events;

import com.esotericsoftware.kryo.util.IntMap;

public class EventHandlers {

    private IntMap<EventHandler[]> eventHandlers = new IntMap<>();

    public <T extends Event> void add(Enum<?> eventType, EventHandler<T> handler) {
        EventHandler[] oldEventHandlers = get(eventType);
        EventHandler[] newEventHandlers;
        if (oldEventHandlers == null) {
            newEventHandlers = new EventHandler[] { handler };
        } else {
            newEventHandlers = new EventHandler[oldEventHandlers.length + 1];
            System.arraycopy(oldEventHandlers, 0, newEventHandlers, 0, oldEventHandlers.length);
            newEventHandlers[oldEventHandlers.length] = handler;
        }
        put(eventType, newEventHandlers);
    }

    public <T extends Event> void put(Enum<?> eventType, EventHandler<T>[] handlers) {
        eventHandlers.put(eventType.ordinal(), handlers);
    }

    public EventHandler[] get(Event event) {
        return get(event.getEventType());
    }

    private EventHandler[] get(Enum<?> eventType) {
        return eventHandlers.get(eventType.ordinal());
    }
}
