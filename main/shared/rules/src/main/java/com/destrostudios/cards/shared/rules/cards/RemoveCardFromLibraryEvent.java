package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class RemoveCardFromLibraryEvent extends Event {
    public int card;

    public RemoveCardFromLibraryEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromLibraryEvent{" + "card=" + card + '}';
    }
}
