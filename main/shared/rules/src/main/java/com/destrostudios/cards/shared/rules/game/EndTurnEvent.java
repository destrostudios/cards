package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndTurnEvent extends Event {

    public int player;

    private EndTurnEvent() {
    }

    public EndTurnEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "EndTurnEvent{" + "player=" + player + '}';
    }
}
