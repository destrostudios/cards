package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCardToBoardHandler extends GameEventHandler<AddCardToBoardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToBoardHandler.class);

    @Override
    public void handle(AddCardToBoardEvent event, NetworkRandom random) {
        LOG.debug("Adding {} to board", inspect(event.card));
        if (data.hasComponent(event.card, Components.CREATURE_CARD)) {
            events.fire(new AddCardToCreatureZoneEvent(event.card), random);
        } else {
            throw new AssertionError("Can't find target zone for " + event.card);
        }
    }
}
