package com.destrostudios.cards.shared.rules.turns.upkeep;

import com.destrostudios.cards.shared.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class EndUpkeepPhaseEvent extends TriggeredEvent {

    public int player;

    public EndUpkeepPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "EndUpkeepPhaseEvent{" + "player=" + player + '}';
    }

}
