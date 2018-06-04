package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class AddCardToEnchantmentZoneEvent extends Event {
    public final int card;

    public AddCardToEnchantmentZoneEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToEnchantmentZoneEvent{" + "card=" + card + '}';
    }
}
