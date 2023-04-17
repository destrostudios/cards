package com.destrostudios.cards.shared.events;

public interface EventHandler<E extends Event, C> {

    void handle(C context, E event);
}
