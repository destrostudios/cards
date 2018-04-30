package com.destrostudios.cards.shared.rules.turns.upkeep;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartUpkeepPhaseEvent extends Event {

    public int player;

    public StartUpkeepPhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "StartUpkeepPhaseEvent{" + "player=" + player + '}';
    }

}
