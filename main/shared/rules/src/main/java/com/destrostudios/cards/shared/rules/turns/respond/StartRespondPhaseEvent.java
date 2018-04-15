package com.destrostudios.cards.shared.rules.turns.respond;

import com.destrostudios.cards.shared.events.TriggeredEvent;

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
