package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveCardFromLibraryHandler extends GameEventHandler<RemoveCardFromLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromLibraryHandler.class);

    @Override
    public void handle(RemoveCardFromLibraryEvent event, NetworkRandom random) {
        LOG.info("Removing " + inspect(event.card) + " from library");
        events.fire(new RemoveCardFromZoneEvent(event.card, Components.LIBRARY), random);
    }
}
