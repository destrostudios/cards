package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class MoveToHandHandler extends BaseMoveToZoneHandler<MoveToHandEvent> {

    public MoveToHandHandler() {
        super(Components.HAND, OTHER_COMPONENTS);
    }
    private static ComponentDefinition[] OTHER_COMPONENTS = new ComponentDefinition[] {
        Components.LIBRARY,
        Components.CREATURE_ZONE,
        Components.GRAVEYARD,
    };

    @Override
    public void handle(MoveToHandEvent event, NetworkRandom random) {
        super.handle(event.card, random);
    }
}
