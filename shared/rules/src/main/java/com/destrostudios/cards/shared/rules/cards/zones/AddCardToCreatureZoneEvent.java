package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class AddCardToCreatureZoneEvent extends Event {
    public final int card;

    public AddCardToCreatureZoneEvent(int card) {
        super(EventType.ADD_CARD_TO_CREATURE_ZONE);
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToCreatureZoneEvent{" + "card=" + card + '}';
    }
}
