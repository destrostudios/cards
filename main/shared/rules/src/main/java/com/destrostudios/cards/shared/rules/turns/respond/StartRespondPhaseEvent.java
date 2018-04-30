package com.destrostudios.cards.shared.rules.turns.respond;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartRespondPhaseEvent extends Event {

    public int player;

    public StartRespondPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "StartRespondPhaseEvent{" + "player=" + player + '}';
    }

}
