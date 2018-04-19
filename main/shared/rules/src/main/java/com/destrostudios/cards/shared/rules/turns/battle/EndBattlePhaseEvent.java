package com.destrostudios.cards.shared.rules.turns.battle;

import com.destrostudios.cards.shared.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class EndBattlePhaseEvent extends TriggeredEvent {

    public int player;

    public EndBattlePhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "EndBattlePhaseEvent{" + "player=" + player + '}';
    }

}
