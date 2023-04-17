package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuffleLibraryHandler extends GameEventHandler<ShuffleLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryHandler.class);

    @Override
    public void handle(ShuffleLibraryEvent event) {
        LOG.debug("Shuffling library of player {}", inspect(event.player));
        IntList newLibraryCards = data.getComponent(event.player, Components.Player.LIBRARY_CARDS).copy();
        newLibraryCards.shuffle(random::nextInt);
        data.setComponent(event.player, Components.Player.LIBRARY_CARDS, newLibraryCards);
    }
}
