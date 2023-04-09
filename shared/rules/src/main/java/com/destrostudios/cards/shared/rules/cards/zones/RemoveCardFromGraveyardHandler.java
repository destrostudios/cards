package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveCardFromGraveyardHandler extends GameEventHandler<RemoveCardFromGraveyardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromGraveyardHandler.class);

    @Override
    public void handle(RemoveCardFromGraveyardEvent event, NetworkRandom random) {
        LOG.debug("Removing {} from graveyard", inspect(event.card));
        events.fire(new RemoveCardFromZoneEvent(event.card, Components.GRAVEYARD), random);
    }
}
