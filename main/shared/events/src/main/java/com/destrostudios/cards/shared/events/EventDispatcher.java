package com.destrostudios.cards.shared.events;

import java.util.*;
import java.util.function.Predicate;

/**
 *
 * @author Philipp
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EventDispatcher {

    private final Map<Predicate, List<EventHandler>> listeners = new LinkedHashMap<>();

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
        for (Map.Entry<Predicate, List<EventHandler>> entry : listeners.entrySet()) {
            if (entry.getKey().test(event.getClass())) {
                List<EventHandler> eventHandlers = entry.getValue();
                if (eventHandlers != null) {
                    for (EventHandler eventHandler : eventHandlers) {
                        eventHandler.onEvent(event);
                        if (event.isCancelled()) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
