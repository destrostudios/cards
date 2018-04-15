package com.etherblood.cards.rules.turns.upkeep;

import com.etherblood.cards.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class StartUpkeepPhaseEvent extends TriggeredEvent {

    public int player;

    public StartUpkeepPhaseEvent(int player) {
        this.player = player;
    }

}
