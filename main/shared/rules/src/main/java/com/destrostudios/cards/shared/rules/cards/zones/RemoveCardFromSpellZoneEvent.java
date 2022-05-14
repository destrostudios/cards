package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class RemoveCardFromSpellZoneEvent extends Event {
    public final int card;

    public RemoveCardFromSpellZoneEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromSpellZoneEvent{" + "card=" + card + '}';
    }
}
