package com.destrostudios.cards.shared.events;

import java.util.HashMap;

/**
 *
 * @author Philipp
 */
public class EventDispatcher {

    private final HashMap<Class<? extends Event>, EventHandler<? extends Event>[]> listeners = new HashMap<>();

    @SafeVarargs
    public final <T extends Event> void setListeners(Class<T> eventType, EventHandler<T>... listeners) {
        if (this.listeners.put(eventType, listeners) != null) {
            throw new IllegalStateException("added listeners for " + eventType + " multiple times");
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void fire(Event event) {
        EventHandler<? extends Event>[] handlers = listeners.get(event.getClass());
        assert handlers != null : "no handlers for " + event;
        for (EventHandler handler : handlers) {
            handler.onEvent(event);
            if (event.isCancelled()) {
                break;
            }
        }
    }
}
