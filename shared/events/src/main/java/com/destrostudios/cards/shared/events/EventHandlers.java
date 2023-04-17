package com.destrostudios.cards.shared.events;

import com.esotericsoftware.kryo.util.IntMap;

public class EventHandlers<C> {

    private IntMap<EventHandler[]> handlers = new IntMap<>();

    public <T extends Event, C> void add(Enum<?> eventType, EventHandler<T, C> handler) {
        EventHandler[] oldHandlers = get(eventType);
        EventHandler[] newHandlers;
        if (oldHandlers == null) {
            newHandlers = new EventHandler[] { handler };
        } else {
            newHandlers = new EventHandler[oldHandlers.length + 1];
            System.arraycopy(oldHandlers, 0, newHandlers, 0, oldHandlers.length);
            newHandlers[oldHandlers.length] = handler;
        }
        put(eventType, newHandlers);
    }

    public <T extends Event> void put(Enum<?> eventType, EventHandler<T, C>[] handlers) {
        this.handlers.put(eventType.ordinal(), handlers);
    }

    public <E extends Event> EventHandler<E, C>[] get(E event) {
        return get(event.getEventType());
    }

    private <E extends Event> EventHandler<E, C>[] get(Enum<?> eventType) {
        return handlers.get(eventType.ordinal());
    }
}
