package com.destrostudios.cards.shared.rules.turns.main;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartMainPhaseEvent extends Event {

    public int player;

    public StartMainPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "StartMainPhaseEvent{" + "player=" + player + '}';
    }

}
