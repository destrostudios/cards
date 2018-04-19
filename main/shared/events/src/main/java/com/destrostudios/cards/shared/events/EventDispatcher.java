package com.destrostudios.cards.shared.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class EventDispatcher {

    private final HashMap<Class<? extends Event>, List<EventHandler<? extends Event>>> listeners = new HashMap<>();

    public <T extends Event> void addListener(Class<T> eventType, EventHandler<T> listener) {
        listeners.computeIfAbsent(eventType, x -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void fire(Event event) {
        List<EventHandler<? extends Event>> handlers = listeners.get(event.getClass());
        assert handlers != null : "no handlers for " + event;
        for (EventHandler handler : handlers) {
            handler.onEvent(event);
            if (event.isCancelled()) {
                break;
            }
        }
    }

}
