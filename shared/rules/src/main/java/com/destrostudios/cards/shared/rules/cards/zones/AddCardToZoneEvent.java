package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.events.Event;

public class AddCardToZoneEvent extends Event {

    public final int card;
    public final ComponentDefinition<Integer> zone;

    public AddCardToZoneEvent(int card, ComponentDefinition<Integer> zone) {
        this.card = card;
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "AddCardToZoneEvent{" + "card=" + card + ", zone=" + zone + '}';
    }
}
