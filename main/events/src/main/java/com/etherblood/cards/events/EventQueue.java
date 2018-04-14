package com.etherblood.cards.events;

/**
 *
 * @author Philipp
 */
public interface EventQueue {
    void action(ActionEvent event);
    void trigger(TriggeredEvent event);
    void response(ResponseEvent event);

}
