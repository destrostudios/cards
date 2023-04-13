package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class RemoveCardFromGraveyardEvent extends Event {
    public final int card;

    public RemoveCardFromGraveyardEvent(int card) {
        super(EventType.REMOVE_CARD_FROM_GRAVEYARD);
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromLibraryEvent{" + "card=" + card + '}';
    }
}
