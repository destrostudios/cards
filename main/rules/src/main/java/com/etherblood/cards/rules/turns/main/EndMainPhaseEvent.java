package com.etherblood.cards.rules.turns.main;

import com.etherblood.cards.events.ActionEvent;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseEvent extends ActionEvent {

    public int player;

    public EndMainPhaseEvent(int player) {
        this.player = player;
    }

}
