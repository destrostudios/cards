package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.ResponseEvent;

/**
 *
 * @author Philipp
 */
public class AddCardToHandEvent extends ResponseEvent {
    public int card;

    public AddCardToHandEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToHandEvent{" + "card=" + card + '}';
    }
}
