package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class MoveToCreatureZoneHandler extends BaseMoveToZoneHandler<MoveToCreatureZoneEvent> {

    public MoveToCreatureZoneHandler() {
        super(Components.CREATURE_ZONE, OTHER_COMPONENTS);
    }
    private static ComponentDefinition[] OTHER_COMPONENTS = new ComponentDefinition[] {
        Components.LIBRARY,
        Components.HAND,
        Components.GRAVEYARD
    };

    @Override
    public void handle(MoveToCreatureZoneEvent event, NetworkRandom random) {
        handle(event.card, random);
    }
}
