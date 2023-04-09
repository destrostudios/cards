package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCardToGraveyardHandler extends GameEventHandler<AddCardToGraveyardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToGraveyardHandler.class);

    @Override
    public void handle(AddCardToGraveyardEvent event, NetworkRandom random) {
        LOG.debug("Adding card " + inspect(event.card) + " to graveyard");
        events.fire(new AddCardToZoneEvent(event.card, Components.GRAVEYARD), random);
    }
}
