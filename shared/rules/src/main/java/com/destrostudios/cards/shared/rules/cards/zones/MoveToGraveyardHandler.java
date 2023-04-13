package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class MoveToGraveyardHandler extends BaseMoveToZoneHandler<MoveToGraveyardEvent> {

    public MoveToGraveyardHandler() {
        super(Components.GRAVEYARD, OTHER_COMPONENTS);
    }
    private static ComponentDefinition[] OTHER_COMPONENTS = new ComponentDefinition[] {
        Components.LIBRARY,
        Components.HAND,
        Components.CREATURE_ZONE
    };

    @Override
    public void handle(MoveToGraveyardEvent event, NetworkRandom random) {
        handle(event.card, random);
    }
}
