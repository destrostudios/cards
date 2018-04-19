package com.destrostudios.cards.shared.rules.turns.main;

import com.destrostudios.cards.shared.events.ActionEvent;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseEvent extends ActionEvent {

    public int player;

    public EndMainPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "EndMainPhaseEvent{" + "player=" + player + '}';
    }

}
