package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveCardFromCreatureZoneHandler extends GameEventHandler<RemoveCardFromCreatureZoneEvent> {

    @Override
    public void handle(RemoveCardFromCreatureZoneEvent event, NetworkRandom random) {
        events.fire(new RemoveCardFromBoardZoneEvent(event.card, Components.CREATURE_ZONE), random);
    }
}
