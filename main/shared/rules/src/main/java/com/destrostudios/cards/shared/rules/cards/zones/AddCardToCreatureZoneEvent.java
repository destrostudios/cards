package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class AddCardToCreatureZoneEvent extends Event {
    public final int card;

    public AddCardToCreatureZoneEvent(int card) {
        this.card = card;
    }
}
