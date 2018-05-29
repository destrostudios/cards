package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseEvent extends Event {

    public int player;

    private EndMainPhaseEvent() {
    }

    public EndMainPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return EndMainPhaseEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
