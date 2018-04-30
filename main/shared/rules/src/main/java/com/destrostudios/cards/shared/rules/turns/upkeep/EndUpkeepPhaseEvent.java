package com.destrostudios.cards.shared.rules.turns.upkeep;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndUpkeepPhaseEvent extends Event {

    public int player;

    public EndUpkeepPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "EndUpkeepPhaseEvent{" + "player=" + player + '}';
    }

}
