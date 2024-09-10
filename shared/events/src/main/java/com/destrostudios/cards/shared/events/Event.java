package com.destrostudios.cards.shared.events;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public abstract class Event {

    public Event(Enum<?> eventType) {
        this.eventType = eventType;
    }
    private Enum<?> eventType;
    private Event parent;
    private Event root;
    private boolean cancelled;

    public void setParent(Event parent) {
        this.parent = parent;
        this.root = ((parent != null) ? parent.root : this);
    }

    public void cancel() {
        this.cancelled = true;
    }

    boolean isSomeParentCancelled() {
        return ((parent != null) && (parent.isCancelled() || parent.isSomeParentCancelled()));
    }
}
