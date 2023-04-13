package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class MoveToCreatureZoneEvent extends Event {
    public final int card;

    public MoveToCreatureZoneEvent(int card) {
        super(EventType.MOVE_TO_CREATURE_ZONE);
        this.card = card;
    }

    @Override
    public String toString() {
        return "MoveToCreatureZoneEvent{" + "card=" + card + '}';
    }
}
