package com.destrostudios.cards.shared.events;

/**
 *
 * @author Philipp
 */
public abstract class Event {

    private Event parent;
    private boolean cancelled;

    public Event getParent() {
        return parent;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    void setParent(Event parent) {
        this.parent = parent;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
