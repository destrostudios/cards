package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class RemoveCardFromLibraryEventHandler implements EventHandler<RemoveCardFromLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromLibraryEventHandler.class);

    private final EntityData data;
    private final EventQueue events;
    private final int libraryKey = Components.LIBRARY, ownedByKey = Components.OWNED_BY;

    public RemoveCardFromLibraryEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(RemoveCardFromLibraryEvent event) {
        int player = data.get(event.card, ownedByKey);
        int libraryIndex = data.get(event.card, libraryKey);
        for (int libraryCard : data.entities(libraryKey, x -> data.hasValue(x, ownedByKey, player), x -> data.get(x, libraryKey) > libraryIndex)) {
            data.set(libraryCard, libraryKey, data.get(libraryCard, libraryKey) - 1);
        }
        data.remove(event.card, libraryKey);
        LOG.info("removed {} from library", event.card);
    }

}
