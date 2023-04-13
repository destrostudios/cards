package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

/**
 *
 * @author Philipp
 */
public class AddCardToLibraryEvent extends Event {
    public final int card;

    public AddCardToLibraryEvent(int card) {
        super(EventType.ADD_CARD_TO_LIBRARY);
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToLibraryEvent{" + "card=" + card + '}';
    }
}
