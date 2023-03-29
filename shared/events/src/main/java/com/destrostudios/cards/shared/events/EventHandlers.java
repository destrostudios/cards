package com.destrostudios.cards.shared.events;

import java.util.*;
import java.util.function.Predicate;

public class EventHandlers {

    private final Map<Predicate, List<EventHandler>> handlers = new LinkedHashMap<>();

    public <T extends Event> void add(Class<T> eventClass, EventHandler<T> handler) {
        add(eventClass::isAssignableFrom, handler);
    }

    public <T extends Event> void add(Predicate<Class<T>> eventClassPredicate, EventHandler<T> handler) {
        List<EventHandler> registeredHandlers = this.handlers.computeIfAbsent(eventClassPredicate, x -> new ArrayList<>());
        registeredHandlers.add(handler);
    }

    public <T extends Event> Iterable<EventHandler> get(Class<T> eventClass) {
        LinkedList<EventHandler> matchingHandlers = new LinkedList<>();
        for (Map.Entry<Predicate, List<EventHandler>> entry : handlers.entrySet()) {
            if (entry.getKey().test(eventClass)) {
                matchingHandlers.addAll(entry.getValue());
            }
        }
        return matchingHandlers;
    }
}
