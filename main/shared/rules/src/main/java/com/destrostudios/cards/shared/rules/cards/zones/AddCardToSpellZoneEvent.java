package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class AddCardToSpellZoneEvent extends Event {
    public final int card;

    public AddCardToSpellZoneEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToSpellZoneEvent{" + "card=" + card + '}';
    }
}
