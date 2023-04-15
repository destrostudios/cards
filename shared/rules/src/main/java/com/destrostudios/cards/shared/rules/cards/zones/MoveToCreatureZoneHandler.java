package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class MoveToCreatureZoneHandler extends BaseMoveToZoneHandler<MoveToCreatureZoneEvent> {

    public MoveToCreatureZoneHandler() {
        super(Components.CREATURE_ZONE, Components.Player.CREATURE_ZONE_CARDS, OTHER_CARD_ZONE_COMPONENTS, OTHER_PLAYER_ZONE_COMPONENTS);
    }
    private static ComponentDefinition[] OTHER_CARD_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.LIBRARY,
        Components.HAND,
        Components.GRAVEYARD
    };
    private static ComponentDefinition[] OTHER_PLAYER_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.Player.LIBRARY_CARDS,
        Components.Player.HAND_CARDS,
        Components.Player.GRAVEYARD_CARDS
    };

    @Override
    public void handle(MoveToCreatureZoneEvent event, NetworkRandom random) {
        handle(event.card, random);
    }
}
