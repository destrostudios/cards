package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveToHandHandler extends GameEventHandler<MoveToHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MoveToHandHandler.class);

    @Override
    public void handle(MoveToHandEvent event, NetworkRandom random) {
        LOG.debug("Moving {} to hand", inspect(event.card));
        events.fire(new MoveToZoneEvent(event.card, Components.HAND), random);
    }
}
