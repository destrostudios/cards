package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;

public class MoveToHandHandler extends BaseMoveToZoneHandler<MoveToHandEvent> {

    public MoveToHandHandler() {
        super(
            Components.Zone.HAND,
            Components.Zone.PLAYER_HAND,
            OTHER_CARD_ZONE_COMPONENTS,
            OTHER_CARD_PLAYER_ZONE_COMPONENTS,
            Components.Player.HAND_CARDS,
            OTHER_PLAYER_ZONE_CARDS_COMPONENTS
        );
    }
    private static ComponentDefinition[] OTHER_CARD_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.Zone.LIBRARY,
        Components.Zone.CREATURE_ZONE,
        Components.Zone.GRAVEYARD
    };
    private static ComponentDefinition[][] OTHER_CARD_PLAYER_ZONE_COMPONENTS = new ComponentDefinition[][] {
        Components.Zone.PLAYER_LIBRARY,
        Components.Zone.PLAYER_CREATURE_ZONE,
        Components.Zone.PLAYER_GRAVEYARD
    };
    private static ComponentDefinition[] OTHER_PLAYER_ZONE_CARDS_COMPONENTS = new ComponentDefinition[] {
        Components.Player.LIBRARY_CARDS,
        Components.Player.CREATURE_ZONE_CARDS,
        Components.Player.GRAVEYARD_CARDS
    };

    @Override
    public void handle(MoveToHandEvent event) {
        super.handle(event.card);
    }
}
