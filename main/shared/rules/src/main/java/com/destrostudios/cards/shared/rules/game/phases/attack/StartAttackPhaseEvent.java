package com.destrostudios.cards.shared.rules.game.phases.attack;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartAttackPhaseEvent extends Event {

    public int player;

    private StartAttackPhaseEvent() {
    }

    public StartAttackPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return StartAttackPhaseEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
