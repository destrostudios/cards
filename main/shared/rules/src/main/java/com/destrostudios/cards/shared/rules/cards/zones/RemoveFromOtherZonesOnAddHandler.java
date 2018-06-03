package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveFromOtherZonesOnAddHandler extends GameEventHandler<AddCardToZoneEvent> {

    @Override
    public void handle(AddCardToZoneEvent event) {
        if ((event.zone != Components.LIBRARY) && data.hasComponent(event.card, Components.LIBRARY)) {
            events.fireSubEvent(new RemoveCardFromLibraryEvent(event.card));
        }
        else if ((event.zone != Components.HAND_CARDS) && data.hasComponent(event.card, Components.HAND_CARDS)) {
            events.fireSubEvent(new RemoveCardFromHandEvent(event.card));
        }
        else if ((event.zone != Components.LAND_ZONE) && data.hasComponent(event.card, Components.LAND_ZONE)) {
            events.fireSubEvent(new RemoveCardFromLandZoneEvent(event.card));
        }
        else if ((event.zone != Components.CREATURE_ZONE) && data.hasComponent(event.card, Components.CREATURE_ZONE)) {
            events.fireSubEvent(new RemoveCardFromCreatureZoneEvent(event.card));
        }
        else if ((event.zone != Components.ENCHANTMENT_ZONE) && data.hasComponent(event.card, Components.ENCHANTMENT_ZONE)) {
            events.fireSubEvent(new RemoveCardFromEnchantmentZoneEvent(event.card));
        }
    }
}
