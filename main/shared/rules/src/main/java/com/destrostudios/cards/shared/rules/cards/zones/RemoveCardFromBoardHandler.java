package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveCardFromBoardHandler extends GameEventHandler<RemoveCardFromBoardEvent> {

    @Override
    public void handle(RemoveCardFromBoardEvent event) {
        if (data.hasComponent(event.card, Components.LAND_CARD)) {
            events.fire(new RemoveCardFromLandZoneEvent(event.card));
        }
        else if (data.hasComponent(event.card, Components.CREATURE_CARD)) {
            events.fire(new RemoveCardFromCreatureZoneEvent(event.card));
        }
        else if (data.hasComponent(event.card, Components.ENCHANTMENT_CARD)) {
            events.fire(new RemoveCardFromEnchantmentZoneEvent(event.card));
        }
        throw new AssertionError("Can't find target zone for " + event.card);
    }
}
