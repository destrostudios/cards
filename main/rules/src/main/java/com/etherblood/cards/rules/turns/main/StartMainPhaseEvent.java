package com.etherblood.cards.rules.turns.main;

import com.etherblood.cards.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class StartMainPhaseEvent extends TriggeredEvent {

    public int player;

    public StartMainPhaseEvent(int player) {
        this.player = player;
    }

}
