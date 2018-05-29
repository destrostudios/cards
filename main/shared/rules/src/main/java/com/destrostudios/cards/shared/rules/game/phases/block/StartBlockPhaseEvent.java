package com.destrostudios.cards.shared.rules.game.phases.block;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartBlockPhaseEvent extends Event {

    public int player;

    private StartBlockPhaseEvent() {
    }

    public StartBlockPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return StartBlockPhaseEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
