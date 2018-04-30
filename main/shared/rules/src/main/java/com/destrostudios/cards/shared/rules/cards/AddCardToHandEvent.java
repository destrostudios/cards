package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class AddCardToHandEvent extends Event {
    public int card;

    public AddCardToHandEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToHandEvent{" + "card=" + card + '}';
    }
}
