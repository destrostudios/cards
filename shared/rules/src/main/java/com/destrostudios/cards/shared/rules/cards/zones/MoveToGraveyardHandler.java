package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class MoveToGraveyardHandler extends BaseMoveToZoneHandler<MoveToGraveyardEvent> {

    public MoveToGraveyardHandler() {
        super(Components.GRAVEYARD, Components.Player.GRAVEYARD_CARDS, OTHER_CARD_COMPONENTS, OTHER_PLAYER_ZONE_COMPONENTS);
    }
    private static ComponentDefinition[] OTHER_CARD_COMPONENTS = new ComponentDefinition[] {
        Components.LIBRARY,
        Components.HAND,
        Components.CREATURE_ZONE
    };
    private static ComponentDefinition[] OTHER_PLAYER_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.Player.LIBRARY_CARDS,
        Components.Player.HAND_CARDS,
        Components.Player.CREATURE_ZONE_CARDS
    };

    @Override
    public void handle(MoveToGraveyardEvent event, NetworkRandom random) {
        handle(event.card, random);
    }
}
