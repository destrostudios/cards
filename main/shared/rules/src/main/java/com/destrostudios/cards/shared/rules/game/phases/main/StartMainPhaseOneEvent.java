package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartMainPhaseOneEvent extends Event {

    public int player;

    private StartMainPhaseOneEvent() {
    }

    public StartMainPhaseOneEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return StartMainPhaseOneEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
