package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class RemoveCardFromLibraryEvent extends Event {
    public final int card;

    public RemoveCardFromLibraryEvent(int card) {
        super(EventType.REMOVE_CARD_FROM_LIBRARY);
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromLibraryEvent{" + "card=" + card + '}';
    }
}
