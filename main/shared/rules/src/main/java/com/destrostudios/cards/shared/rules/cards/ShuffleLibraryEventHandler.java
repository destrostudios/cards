package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ShuffleLibraryEventHandler implements EventHandler<ShuffleLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryEventHandler.class);

    private final EntityData data;
    private final EventQueue events;
    private final Random random;
    private final int libraryKey = Components.LIBRARY, ownedByKey = Components.OWNED_BY;

    public ShuffleLibraryEventHandler(EntityData data, EventQueue events, Random random) {
        this.data = data;
        this.events = events;
        this.random = random;
    }

    @Override
    public void onEvent(ShuffleLibraryEvent event) {
        IntArrayList libraryCards = data.entities(libraryKey, card -> data.hasValue(card, ownedByKey, event.player));
        libraryCards.shuffle(random);
        for (int i = 0; i < libraryCards.size(); i++) {
            int card = libraryCards.get(i);
            data.set(card, libraryKey, i);
        }
        LOG.info("shuffled library of {}", event.player);
    }

}
