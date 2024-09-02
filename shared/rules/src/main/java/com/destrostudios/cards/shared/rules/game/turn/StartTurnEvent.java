package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class StartTurnEvent extends Event {

    public int player;

    public StartTurnEvent(int player) {
        super(EventType.START_TURN);
        this.player = player;
    }

    @Override
    public String toString() {
        return StartTurnEvent.class.getSimpleName() + "{player=" + player + '}';
    }
}
