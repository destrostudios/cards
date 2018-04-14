package com.etherblood.cards.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class EventDispatcherImpl implements EventDispatcher {

    private final HashMap<Class<? extends Event>, List<EventHandler<? extends Event>>> listeners = new HashMap<>();

    public <T extends Event> void addListener(Class<T> eventType, EventHandler<T> listener) {
        listeners.computeIfAbsent(eventType, x -> new ArrayList<>()).add(listener);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void fire(Event event) {
        for (EventHandler handler : listeners.get(event.getClass())) {
            handler.onEvent(event);
            if(event.isCancelled()) {
                break;
            }
        }
    }

}
