package com.destrostudios.cards.shared.events;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Event {

    public Event(Enum<?> eventType) {
        this.eventType = eventType;
    }
    @Getter(AccessLevel.PACKAGE)
    private Enum<?> eventType;
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Event parent;
    private boolean root;
    @Getter(AccessLevel.PACKAGE)
    private boolean cancelled;

    public void markAsRoot() {
        root = true;
    }

    Event getRoot() {
        return (root || (parent == null)) ? this : parent.getRoot();
    }

    public void cancel() {
        this.cancelled = true;
    }

    boolean isSomeParentCancelled() {
        return ((parent != null) && parent.isSomeParentCancelled());
    }
}
