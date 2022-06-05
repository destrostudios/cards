package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class AddCardToGraveyardHandler extends GameEventHandler<AddCardToGraveyardEvent> {

    @Override
    public void handle(AddCardToGraveyardEvent event, NetworkRandom random) {
        events.fire(new AddCardToZoneEvent(event.card, Components.GRAVEYARD), random);
    }
}
