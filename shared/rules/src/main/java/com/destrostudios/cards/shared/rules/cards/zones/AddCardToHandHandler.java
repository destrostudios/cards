package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCardToHandHandler extends GameEventHandler<AddCardToHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToHandHandler.class);

    @Override
    public void handle(AddCardToHandEvent event, NetworkRandom random) {
        events.fire(new AddCardToZoneEvent(event.card, Components.HAND), random);
        LOG.info("added {} to hand", event.card);
    }
}
