package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class EndTurnEvent extends Event {

    public int player;

    // Used by serializer
    private EndTurnEvent() {
        this(0);
    }

    public EndTurnEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return EndTurnEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
