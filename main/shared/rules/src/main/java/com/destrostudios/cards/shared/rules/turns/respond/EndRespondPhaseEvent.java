package com.destrostudios.cards.shared.rules.turns.respond;

import com.destrostudios.cards.shared.events.ActionEvent;

/**
 *
 * @author Philipp
 */
public class EndRespondPhaseEvent extends ActionEvent {

    public int player;

    public EndRespondPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "EndRespondPhaseEvent{" + "player=" + player + '}';
    }

}
