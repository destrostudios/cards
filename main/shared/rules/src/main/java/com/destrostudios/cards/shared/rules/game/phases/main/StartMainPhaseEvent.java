package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartMainPhaseEvent extends Event {

    public int player;

    private StartMainPhaseEvent() {
    }

    public StartMainPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return StartMainPhaseEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
