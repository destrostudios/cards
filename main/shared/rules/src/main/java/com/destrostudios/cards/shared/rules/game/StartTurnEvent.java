package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.events.Event;

public class StartTurnEvent extends Event {

    public int player;

    private StartTurnEvent() {
    }

    public StartTurnEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "StartTurnEvent{" + "player=" + player + '}';
    }
}
