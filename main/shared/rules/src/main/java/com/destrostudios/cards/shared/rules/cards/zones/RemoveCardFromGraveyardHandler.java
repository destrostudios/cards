package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveCardFromGraveyardHandler extends GameEventHandler<RemoveCardFromGraveyardEvent> {

    @Override
    public void handle(RemoveCardFromGraveyardEvent event, NetworkRandom random) {
        events.fire(new RemoveCardFromZoneEvent(event.card, Components.GRAVEYARD), random);
    }
}
