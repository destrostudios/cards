package com.etherblood.cards.rules.turns.respond;

import com.etherblood.cards.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class StartRespondPhaseEvent extends TriggeredEvent {

    public int player;

    public StartRespondPhaseEvent(int player) {
        this.player = player;
    }

}
