package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveCardFromBoardZoneHandler extends GameEventHandler<RemoveCardFromBoardZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromBoardZoneHandler.class);

    @Override
    public void handle(RemoveCardFromBoardZoneEvent event, NetworkRandom random) {
        LOG.info("Removing " + inspect(event.card) + " from board zone " + event.zone.getName());
        events.fire(new RemoveCardFromZoneEvent(event.card, event.zone), random);
        data.removeComponent(event.card, Components.BOARD);
    }
}
