package com.destrostudios.cards.shared.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class EventDispatcher {

    private final Map<Class<? extends Event>, List<EventHandler<? extends Event>>> listeners = new HashMap<>();

    @SafeVarargs
    public final <T extends Event> void addListeners(Class<T> eventType, EventHandler<T>... listeners) {
        addListeners(eventType, Arrays.asList(listeners));
    }

    public <T extends Event> void addListeners(Class<T> eventType, List<EventHandler<T>> listeners) {
        this.listeners.computeIfAbsent(eventType, x -> new ArrayList<>()).addAll(listeners);
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
