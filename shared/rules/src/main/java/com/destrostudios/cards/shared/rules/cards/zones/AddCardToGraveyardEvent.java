package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class AddCardToGraveyardEvent extends Event {
    public final int card;

    public AddCardToGraveyardEvent(int card) {
        super(EventType.ADD_CARD_TO_GRAVEYARD);
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToGraveyardEvent{" + "card=" + card + '}';
    }
}
