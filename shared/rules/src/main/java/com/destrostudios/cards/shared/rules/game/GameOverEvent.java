package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class GameOverEvent extends Event {

    public final int winner;

    public GameOverEvent(int winner) {
        super(EventType.GAME_OVER);
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "GameOverEvent{" + "winner=" + winner + '}';
    }
}
