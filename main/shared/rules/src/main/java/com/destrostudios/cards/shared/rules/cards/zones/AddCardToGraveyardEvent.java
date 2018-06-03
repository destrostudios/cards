package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class AddCardToGraveyardEvent extends Event {
    public final int card;

    public AddCardToGraveyardEvent(int card) {
        this.card = card;
    }
}
