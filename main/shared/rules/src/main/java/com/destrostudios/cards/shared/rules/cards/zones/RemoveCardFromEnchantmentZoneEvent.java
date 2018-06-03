package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class RemoveCardFromEnchantmentZoneEvent extends Event {
    public final int card;

    public RemoveCardFromEnchantmentZoneEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromEnchantmentZoneEvent{" + "card=" + card + '}';
    }
}
