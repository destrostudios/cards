package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCardToCreatureZoneHandler extends GameEventHandler<AddCardToCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToCreatureZoneHandler.class);

    @Override
    public void handle(AddCardToCreatureZoneEvent event, NetworkRandom random) {
        LOG.debug("Adding {} to creature zone", inspect(event.card));
        events.fire(new AddCardToZoneEvent(event.card, Components.CREATURE_ZONE), random);
    }
}
