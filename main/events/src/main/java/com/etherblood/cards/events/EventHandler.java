package com.etherblood.cards.events;

/**
 *
 * @author Philipp
 */
public interface EventHandler<E extends Event> {

    void onEvent(E event);
}
