package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class RemovedFromCreatureZoneEvent extends Event {
    public final int card;

    public RemovedFromCreatureZoneEvent(int card) {
        super(EventType.REMOVED_FROM_CREATURE_ZONE);
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemovedFromCreatureZoneEvent{" + "card=" + card + '}';
    }
}
