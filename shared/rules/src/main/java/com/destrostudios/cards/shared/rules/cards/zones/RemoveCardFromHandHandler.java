package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveCardFromHandHandler extends GameEventHandler<RemoveCardFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromHandHandler.class);

    @Override
    public void handle(RemoveCardFromHandEvent event, NetworkRandom random) {
        LOG.debug("Removing " + inspect(event.card) + " from hand");
        events.fire(new RemoveCardFromZoneEvent(event.card, Components.HAND), random);
    }
}
