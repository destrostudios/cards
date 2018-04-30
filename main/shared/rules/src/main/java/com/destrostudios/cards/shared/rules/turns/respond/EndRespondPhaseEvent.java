package com.destrostudios.cards.shared.rules.turns.respond;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndRespondPhaseEvent extends Event {

    public int player;

    public EndRespondPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "EndRespondPhaseEvent{" + "player=" + player + '}';
    }

}
