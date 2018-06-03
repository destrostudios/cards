package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class AddCardToLandZoneEvent extends Event {
    public final int card;

    public AddCardToLandZoneEvent(int card) {
        this.card = card;
    }
}
