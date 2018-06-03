package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class RemoveCardFromLandZoneEvent extends Event {
    public final int card;

    public RemoveCardFromLandZoneEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromLandZoneEvent{" + "card=" + card + '}';
    }
}
