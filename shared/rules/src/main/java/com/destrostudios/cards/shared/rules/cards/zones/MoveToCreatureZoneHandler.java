package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;

public class MoveToCreatureZoneHandler extends BaseMoveToZoneHandler<MoveToCreatureZoneEvent> {

    public MoveToCreatureZoneHandler() {
        super(
            Components.Zone.CREATURE_ZONE,
            Components.Zone.PLAYER_CREATURE_ZONE,
            OTHER_CARD_ZONE_COMPONENTS,
            OTHER_CARD_PLAYER_ZONE_COMPONENTS,
            Components.Player.CREATURE_ZONE_CARDS,
            OTHER_PLAYER_ZONE_CARDS_COMPONENTS
        );
    }
    private static ComponentDefinition[] OTHER_CARD_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.Zone.LIBRARY,
        Components.Zone.HAND,
        Components.Zone.GRAVEYARD
    };
    private static ComponentDefinition[][] OTHER_CARD_PLAYER_ZONE_COMPONENTS = new ComponentDefinition[][] {
        Components.Zone.PLAYER_LIBRARY,
        Components.Zone.PLAYER_HAND,
        Components.Zone.PLAYER_GRAVEYARD
    };
    private static ComponentDefinition[] OTHER_PLAYER_ZONE_CARDS_COMPONENTS = new ComponentDefinition[] {
        Components.Player.LIBRARY_CARDS,
        Components.Player.HAND_CARDS,
        Components.Player.GRAVEYARD_CARDS
    };

    @Override
    public void handle(MoveToCreatureZoneEvent event) {
        handle(event.card);
    }
}
