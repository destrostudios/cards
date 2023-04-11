package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuffleLibraryHandler extends GameEventHandler<ShuffleLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryHandler.class);

    @Override
    public void handle(ShuffleLibraryEvent event, NetworkRandom random) {
        LOG.debug("Shuffling library of player {}", inspect(event.player));
        IntList libraryCards = data.query(Components.LIBRARY).list(card -> data.getComponent(card, Components.OWNED_BY) == event.player);
        for (int i = libraryCards.size(); i > 0; i--) {
            int cardIndex = random.nextInt(i);
            int cardEntity = libraryCards.removeAt(cardIndex);
            data.setComponent(cardEntity, Components.LIBRARY, i - 1);
        }
    }
}
