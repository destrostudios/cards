package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCardToLibraryHandler extends GameEventHandler<AddCardToLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToLibraryHandler.class);

    @Override
    public void handle(AddCardToLibraryEvent event, NetworkRandom random) {
        events.fire(new AddCardToZoneEvent(event.card, Components.LIBRARY), random);
        LOG.info("added {} to library", event.card);
    }
}
