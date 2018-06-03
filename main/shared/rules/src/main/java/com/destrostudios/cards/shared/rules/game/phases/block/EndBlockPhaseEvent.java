package com.destrostudios.cards.shared.rules.game.phases.block;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndBlockPhaseEvent extends Event {

    public int player;

    // Used by serializer
    private EndBlockPhaseEvent() {
        this(0);
    }

    public EndBlockPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return EndBlockPhaseEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
