package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class MoveToLibraryHandler extends BaseMoveToZoneHandler<MoveToLibraryEvent> {

    public MoveToLibraryHandler() {
        super(Components.LIBRARY, Components.Player.LIBRARY_CARDS, OTHER_CARD_ZONE_COMPONENTS, OTHER_PLAYER_ZONE_COMPONENTS);
    }
    private static ComponentDefinition[] OTHER_CARD_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.HAND,
        Components.CREATURE_ZONE,
        Components.GRAVEYARD,
    };
    private static ComponentDefinition[] OTHER_PLAYER_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.Player.HAND_CARDS,
        Components.Player.CREATURE_ZONE_CARDS,
        Components.Player.GRAVEYARD_CARDS
    };

    @Override
    public void handle(MoveToLibraryEvent event, NetworkRandom random) {
        handle(event.card, random);
    }
}
