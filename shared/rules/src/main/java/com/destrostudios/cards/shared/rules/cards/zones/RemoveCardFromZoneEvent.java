package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class RemoveCardFromZoneEvent extends Event {

    public final int card;
    public final ComponentDefinition<Integer> zone;

    public RemoveCardFromZoneEvent(int card, ComponentDefinition<Integer> zone) {
        super(EventType.REMOVE_CARD_FROM_ZONE);
        this.card = card;
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "RemoveCardFromZoneEvent{" + "card=" + card + ", zone=" + zone + '}';
    }
}
