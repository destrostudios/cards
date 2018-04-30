package com.destrostudios.cards.shared.rules.turns.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndBattlePhaseEvent extends Event {

    public int player;

    public EndBattlePhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "EndBattlePhaseEvent{" + "player=" + player + '}';
    }

}
