package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.ResponseEvent;

/**
 *
 * @author Philipp
 */
public class DrawCardEvent extends ResponseEvent {

    public int player;
    public int card;

    public DrawCardEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "DrawCardEvent{" + "player=" + player + ", card=" + card + '}';
    }
}
