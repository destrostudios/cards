package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 * @author Philipp
 */
public class DrawCardEvent extends Event {

    public int player;

    // Used by serializer
    private DrawCardEvent() { }

    public DrawCardEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "DrawCardEvent{" + "player=" + player + '}';
    }
}
