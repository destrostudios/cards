package com.etherblood.cards.rules.turns.upkeep;

import com.etherblood.cards.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class EndUpkeepPhaseEvent extends TriggeredEvent {

    public int player;

    public EndUpkeepPhaseEvent(int player) {
        this.player = player;
    }

}
