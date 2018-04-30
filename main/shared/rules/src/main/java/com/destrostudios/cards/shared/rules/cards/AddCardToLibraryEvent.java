package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class AddCardToLibraryEvent extends Event {
    public int card;

    public AddCardToLibraryEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToLibraryEvent{" + "card=" + card + '}';
    }
}
