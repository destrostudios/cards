package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class RemoveCardFromCreatureZoneEvent extends Event {
    public final int card;

    public RemoveCardFromCreatureZoneEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromCreatureZoneEvent{" + "card=" + card + '}';
    }
}
