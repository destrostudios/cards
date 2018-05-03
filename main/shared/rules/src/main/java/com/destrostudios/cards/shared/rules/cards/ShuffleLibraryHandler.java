package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.IntUnaryOperator;

/**
 * @author Philipp
 */
public class ShuffleLibraryHandler implements EventHandler<ShuffleLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryHandler.class);

    private final EntityData data;
    private final EventQueue events;
    private final IntUnaryOperator random;

    public ShuffleLibraryHandler(EntityData data, EventQueue events, IntUnaryOperator random) {
        this.data = data;
        this.events = events;
        this.random = random;
    }

    @Override
    public void onEvent(ShuffleLibraryEvent event) {
        IntArrayList libraryCards = data.entities(Components.LIBRARY, (cardEntity) -> data.getComponent(cardEntity, Components.OWNED_BY) == event.player);
        libraryCards.shuffle(random);

        for (int i = 0; i < libraryCards.size(); i++) {
            int cardEntity = libraryCards.get(i);
            data.setComponent(cardEntity, Components.LIBRARY, i);
        }
    }

}
