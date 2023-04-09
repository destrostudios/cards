package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveCardFromCreatureZoneHandler extends GameEventHandler<RemoveCardFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromCreatureZoneHandler.class);

    @Override
    public void handle(RemoveCardFromCreatureZoneEvent event, NetworkRandom random) {
        LOG.debug("Removing {} from creature zone", inspect(event.card));
        events.fire(new RemoveCardFromZoneEvent(event.card, Components.CREATURE_ZONE), random);
    }
}
