package com.destrostudios.cards.shared.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author Philipp
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EventDispatcher {

    private final Map<Predicate, List<EventHandler>> listeners = new HashMap<>();

    public <T extends Event> void addListeners(Class<T> eventType, EventHandler<T>... listeners) {
        addListeners(eventType::isAssignableFrom, listeners);
    }

    @SafeVarargs
    public final <T extends Event> void addListeners(Predicate<Class<T>> eventType, EventHandler<T>... listeners) {
        addListeners(eventType, Arrays.asList(listeners));
    }

    public <T extends Event> void addListeners(Predicate<Class<T>> eventType, List<EventHandler<T>> listeners) {
        this.listeners.computeIfAbsent(eventType, x -> new ArrayList<>()).addAll(listeners);
    }

    public void fire(Event event) {
        boolean found = false;
        for (Map.Entry<Predicate, List<EventHandler>> entry : listeners.entrySet()) {
            if (entry.getKey().test(event.getClass())) {
                found = true;
                for (EventHandler eventHandler : entry.getValue()) {
                    eventHandler.onEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                }
            }
        }
        assert found : "no handlers for " + event;
    }
}
