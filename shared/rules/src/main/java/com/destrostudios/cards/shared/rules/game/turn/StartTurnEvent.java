package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class StartTurnEvent extends Event {

    public int player;

    public StartTurnEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return StartTurnEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}