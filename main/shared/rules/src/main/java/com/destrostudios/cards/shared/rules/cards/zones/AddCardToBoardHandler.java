package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddCardToBoardHandler extends GameEventHandler<AddCardToBoardEvent> {

    @Override
    public void handle(AddCardToBoardEvent event) {
        if (data.hasComponent(event.card, Components.LAND_CARD)) {
            events.fireSubEvent(new AddCardToLandZoneEvent(event.card));
        }
        else if (data.hasComponent(event.card, Components.CREATURE_CARD)) {
            events.fireSubEvent(new AddCardToCreatureZoneEvent(event.card));
        }
        else if (data.hasComponent(event.card, Components.ENCHANTMENT_CARD)) {
            events.fireSubEvent(new AddCardToEnchantmentZoneEvent(event.card));
        }
        else {
            throw new AssertionError("Can't find target zone for " + event.card);
        }
    }
}
