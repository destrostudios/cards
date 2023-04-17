package com.destrostudios.cards.shared.events;

public class EventHandling<C> {

    protected EventHandlers<C> pre = new EventHandlers();
    protected EventHandlers<C> instant = new EventHandlers();
    protected EventHandlers<C> resolved = new EventHandlers();

    public EventHandlers<C> pre() {
        return pre;
    }

    public EventHandlers<C> instant() {
        return instant;
    }

    public EventHandlers<C> resolved() {
        return resolved;
    }
}
