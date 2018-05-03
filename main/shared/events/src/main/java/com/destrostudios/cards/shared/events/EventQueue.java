package com.destrostudios.cards.shared.events;

/**
 *
 * @author Philipp
 */
public interface EventQueue {

    void fireActionEvent(Event event);

    void fireChainEvent(Event event);

    void fireSubEvent(Event event);

}
