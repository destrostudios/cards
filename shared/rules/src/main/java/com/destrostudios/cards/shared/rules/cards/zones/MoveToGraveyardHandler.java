package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;

public class MoveToGraveyardHandler extends BaseMoveToZoneHandler<MoveToGraveyardEvent> {

    public MoveToGraveyardHandler() {
        super(
            Components.Zone.GRAVEYARD,
            Components.Zone.PLAYER_GRAVEYARD,
            OTHER_CARD_ZONE_COMPONENTS,
            OTHER_CARD_PLAYER_ZONE_COMPONENTS,
            Components.Player.GRAVEYARD_CARDS,
            OTHER_PLAYER_ZONE_CARDS_COMPONENTS
        );
    }
    private static ComponentDefinition[] OTHER_CARD_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.Zone.LIBRARY,
        Components.Zone.HAND,
        Components.Zone.CREATURE_ZONE
    };
    private static ComponentDefinition[][] OTHER_CARD_PLAYER_ZONE_COMPONENTS = new ComponentDefinition[][] {
        Components.Zone.PLAYER_LIBRARY,
        Components.Zone.PLAYER_HAND,
        Components.Zone.PLAYER_CREATURE_ZONE
    };
    private static ComponentDefinition[] OTHER_PLAYER_ZONE_CARDS_COMPONENTS = new ComponentDefinition[] {
        Components.Player.LIBRARY_CARDS,
        Components.Player.HAND_CARDS,
        Components.Player.CREATURE_ZONE_CARDS
    };

    @Override
    public void handle(GameContext context, MoveToGraveyardEvent event) {
        handle(context, event.card);
    }
}
