package com.destrostudios.cards.shared.events;

/**
 *
 * @author Philipp
 */
public interface EventHandler<E extends Event> {

    void onEvent(E event);
}
