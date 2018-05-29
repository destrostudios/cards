package com.destrostudios.cards.shared.rules.game.phases.attack;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndAttackPhaseEvent extends Event {

    public int player;

    private EndAttackPhaseEvent() {
    }

    public EndAttackPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return EndAttackPhaseEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
