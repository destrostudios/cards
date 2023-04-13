package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveToCreatureZoneHandler extends GameEventHandler<MoveToCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MoveToCreatureZoneHandler.class);

    @Override
    public void handle(MoveToCreatureZoneEvent event, NetworkRandom random) {
        LOG.debug("Moving {} to creature zone", inspect(event.card));
        events.fire(new MoveToZoneEvent(event.card, Components.CREATURE_ZONE), random);
    }
}
