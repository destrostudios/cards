package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveToLibraryHandler extends GameEventHandler<MoveToLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MoveToLibraryHandler.class);

    @Override
    public void handle(MoveToLibraryEvent event, NetworkRandom random) {
        LOG.debug("Moving {} to library", inspect(event.card));
        events.fire(new MoveToZoneEvent(event.card, Components.LIBRARY), random);
    }
}
