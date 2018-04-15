package com.etherblood.cards.rules.turns.battle;

import com.etherblood.cards.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class StartBattlePhaseEvent extends TriggeredEvent {

    public int player;

    public StartBattlePhaseEvent(int player) {
        this.player = player;
    }

}
