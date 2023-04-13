package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class MoveToLibraryHandler extends BaseMoveToZoneHandler<MoveToLibraryEvent> {

    public MoveToLibraryHandler() {
        super(Components.LIBRARY, OTHER_COMPONENTS);
    }
    private static ComponentDefinition[] OTHER_COMPONENTS = new ComponentDefinition[] {
        Components.HAND,
        Components.CREATURE_ZONE,
        Components.GRAVEYARD,
    };

    @Override
    public void handle(MoveToLibraryEvent event, NetworkRandom random) {
        handle(event.card, random);
    }
}
