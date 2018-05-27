package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 *
 */
public class AddCardToBoardHandler extends GameEventHandler<AddCardToBoardEvent> {

    @Override
    public void handle(AddCardToBoardEvent event) {
        ComponentDefinition<Integer> zoneComponent = null;
        if (data.hasComponent(event.card, Components.LAND_CARD)) {
            zoneComponent = Components.LAND_ZONE;
        }
        else if (data.hasComponent(event.card, Components.CREATURE_CARD)) {
            zoneComponent = Components.CREATURE_ZONE;
        }
        else if (data.hasComponent(event.card, Components.ENCHANTMENT_CARD)) {
            zoneComponent = Components.ENCHANTMENT_ZONE;
        }

        int playerEntity = data.getComponent(event.card, Components.OWNED_BY);
        data.setComponent(event.card, zoneComponent, data.query(zoneComponent).count(hasComponentValue(Components.OWNED_BY, playerEntity)));
        data.setComponent(event.card, Components.BOARD);
    }
}
