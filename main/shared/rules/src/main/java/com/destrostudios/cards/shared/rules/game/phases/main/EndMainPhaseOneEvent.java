package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseOneEvent extends Event {

    public int player;

    // Used by serializer
    private EndMainPhaseOneEvent() {
        this(0);
    }

    public EndMainPhaseOneEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return EndMainPhaseOneEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
