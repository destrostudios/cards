package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCardToBoardZoneHandler extends GameEventHandler<AddCardToBoardZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToBoardZoneHandler.class);

    @Override
    public void handle(AddCardToBoardZoneEvent event, NetworkRandom random) {
        LOG.info("Adding " + inspect(event.card) + " to board zone " + event.zone.getName());
        events.fire(new AddCardToZoneEvent(event.card, event.zone), random);
        data.setComponent(event.card, Components.BOARD);
    }
}
