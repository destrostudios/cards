package com.destrostudios.cards.shared.rules.turns.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartBattlePhaseEvent extends Event {

    public int player;

    public StartBattlePhaseEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "StartBattlePhaseEvent{" + "player=" + player + '}';
    }

}
