package com.destrostudios.cards.shared.events;

/**
 *
 * @author Philipp
 */
public abstract class Event {

    private Event parent;
    private boolean cancelled;

    void setParent(Event parent) {
        this.parent = parent;
    }

    Event getParent() {
        return parent;
    }

    Event getRoot() {
        return ((parent != null) ? parent.getRoot() : this);
    }

    public boolean isCancelled() {
        if ((parent != null) && parent.isCancelled()) {
            return true;
        }
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
