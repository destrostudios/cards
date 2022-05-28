package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class AddCardToCreatureZoneHandler extends GameEventHandler<AddCardToCreatureZoneEvent> {

    @Override
    public void handle(AddCardToCreatureZoneEvent event, NetworkRandom random) {
        events.fire(new AddCardToBoardZoneEvent(event.card, Components.CREATURE_ZONE), random);
    }
}
