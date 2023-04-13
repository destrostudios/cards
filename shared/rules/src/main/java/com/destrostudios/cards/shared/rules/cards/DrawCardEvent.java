package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class DrawCardEvent extends Event {

    public int player;

    public DrawCardEvent(int player) {
        super(EventType.DRAW_CARD);
        this.player = player;
    }

    @Override
    public String toString() {
        return "DrawCardEvent{" + "player=" + player + '}';
    }
}
