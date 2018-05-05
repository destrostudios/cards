package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Philipp
 */
public class ShuffleLibraryHandler extends GameEventHandler<ShuffleLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryHandler.class);

    @Override
    public void handle(ShuffleLibraryEvent event) {
        IntArrayList libraryCards = data.entities(Components.LIBRARY, (cardEntity) -> data.getComponent(cardEntity, Components.OWNED_BY) == event.player);
        libraryCards.shuffle(random);

        for (int i = 0; i < libraryCards.size(); i++) {
            int cardEntity = libraryCards.get(i);
            data.setComponent(cardEntity, Components.LIBRARY, i);
        }
    }

}
