package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class MoveToZoneEvent extends Event {

    public final int card;
    public final ComponentDefinition<Integer> zone;

    public MoveToZoneEvent(int card, ComponentDefinition<Integer> zone) {
        super(EventType.MOVE_TO_ZONE);
        this.card = card;
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "MoveToZoneEvent{" + "card=" + card + ", zone=" + zone + '}';
    }
}
