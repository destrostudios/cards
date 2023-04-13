package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class RemoveCardFromCreatureZoneEvent extends Event {
    public final int card;

    public RemoveCardFromCreatureZoneEvent(int card) {
        super(EventType.REMOVE_CARD_FROM_CREATURE_ZONE);
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromCreatureZoneEvent{" + "card=" + card + '}';
    }
}
