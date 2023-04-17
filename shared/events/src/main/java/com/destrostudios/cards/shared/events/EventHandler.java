package com.destrostudios.cards.shared.events;

public interface EventHandler<E extends Event, C> {

    void onEvent(E event, C context);
}
