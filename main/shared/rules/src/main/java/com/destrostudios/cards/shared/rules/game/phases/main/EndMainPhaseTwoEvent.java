package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseTwoEvent extends Event {

    public int player;

    private EndMainPhaseTwoEvent() {
    }

    public EndMainPhaseTwoEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return EndMainPhaseTwoEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
