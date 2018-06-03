package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddCardToEnchantmentZoneHandler extends GameEventHandler<AddCardToEnchantmentZoneEvent> {

    @Override
    public void handle(AddCardToEnchantmentZoneEvent event) {
        events.fireSubEvent(new AddCardToBoardZoneEvent(event.card, Components.ENCHANTMENT_ZONE));
    }
}
