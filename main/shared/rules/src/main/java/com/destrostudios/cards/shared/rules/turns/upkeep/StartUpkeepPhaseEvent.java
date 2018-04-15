package com.destrostudios.cards.shared.rules.turns.upkeep;

import com.destrostudios.cards.shared.events.TriggeredEvent;

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
