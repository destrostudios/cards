package com.destrostudios.cards.shared.events;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Event {

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Event parent;
    private boolean cancelled;

    Event getRoot() {
        return ((parent != null) ? parent.getRoot() : this);
    }

    public void cancel() {
        this.cancelled = true;
    }

    boolean isCancelled() {
        if ((parent != null) && parent.isCancelled()) {
            return true;
        }
        return cancelled;
    }
}
