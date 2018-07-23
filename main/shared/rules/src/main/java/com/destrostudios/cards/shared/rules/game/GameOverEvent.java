package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.events.Event;

public class GameOverEvent extends Event {

    public final int winner;

    public GameOverEvent(int winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "GameOverEvent{" + "winner=" + winner + '}';
    }
}
