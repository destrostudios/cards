package com.etherblood.cards.rules.turns.battle;

import com.etherblood.cards.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class EndBattlePhaseEvent extends TriggeredEvent {

    public int player;

    public EndBattlePhaseEvent(int player) {
        this.player = player;
    }

}
