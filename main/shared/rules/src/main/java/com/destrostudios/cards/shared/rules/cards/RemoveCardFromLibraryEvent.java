package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.ResponseEvent;

/**
 *
 * @author Philipp
 */
public class RemoveCardFromLibraryEvent extends ResponseEvent {
    public int card;

    public RemoveCardFromLibraryEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromLibraryEvent{" + "card=" + card + '}';
    }
}
