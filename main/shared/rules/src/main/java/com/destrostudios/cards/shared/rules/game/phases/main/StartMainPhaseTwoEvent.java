package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartMainPhaseTwoEvent extends Event {

    public int player;

    // Used by serializer
    private StartMainPhaseTwoEvent() {
        this(0);
    }

    public StartMainPhaseTwoEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return StartMainPhaseTwoEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
