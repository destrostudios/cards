package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class MoveToHandHandler extends BaseMoveToZoneHandler<MoveToHandEvent> {

    public MoveToHandHandler() {
        super(Components.HAND, Components.Player.HAND_CARDS, OTHER_CARD_COMPONENTS, OTHER_PLAYER_ZONE_COMPONENTS);
    }
    private static ComponentDefinition[] OTHER_CARD_COMPONENTS = new ComponentDefinition[] {
        Components.LIBRARY,
        Components.CREATURE_ZONE,
        Components.GRAVEYARD,
    };
    private static ComponentDefinition[] OTHER_PLAYER_ZONE_COMPONENTS = new ComponentDefinition[] {
        Components.Player.LIBRARY_CARDS,
        Components.Player.CREATURE_ZONE_CARDS,
        Components.Player.GRAVEYARD_CARDS
    };

    @Override
    public void handle(MoveToHandEvent event, NetworkRandom random) {
        super.handle(event.card, random);
    }
}
