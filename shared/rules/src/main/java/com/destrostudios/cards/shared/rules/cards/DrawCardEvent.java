package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

public class DrawCardEvent extends Event {

    public int player;

    public DrawCardEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "DrawCardEvent{" + "player=" + player + '}';
    }
}
