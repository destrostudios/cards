package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveCardFromEnchantmentZoneHandler extends GameEventHandler<RemoveCardFromEnchantmentZoneEvent> {

    @Override
    public void handle(RemoveCardFromEnchantmentZoneEvent event) {
        events.fire(new RemoveCardFromBoardZoneEvent(event.card, Components.ENCHANTMENT_ZONE));
    }
}
