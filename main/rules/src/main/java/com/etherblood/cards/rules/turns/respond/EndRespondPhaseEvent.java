package com.etherblood.cards.rules.turns.respond;

import com.etherblood.cards.events.ActionEvent;

/**
 *
 * @author Philipp
 */
public class EndRespondPhaseEvent extends ActionEvent {

    public int player;

    public EndRespondPhaseEvent(int player) {
        this.player = player;
    }

}
