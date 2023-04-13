package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveToGraveyardHandler extends GameEventHandler<MoveToGraveyardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MoveToGraveyardHandler.class);

    @Override
    public void handle(MoveToGraveyardEvent event, NetworkRandom random) {
        LOG.debug("Moving {} to graveyard", inspect(event.card));
        events.fire(new MoveToZoneEvent(event.card, Components.GRAVEYARD), random);
    }
}
