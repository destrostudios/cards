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
public class AddCardToLibraryEventHandler implements EventHandler<AddCardToLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToLibraryEventHandler.class);

    private final EntityData data;
    private final EventQueue events;
    private final int libraryKey = Components.LIBRARY, ownedByKey = Components.OWNED_BY;

    public AddCardToLibraryEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(AddCardToLibraryEvent event) {
        int player = data.get(event.card, ownedByKey);
        int librarySize = data.entities(libraryKey, entity -> data.hasValue(entity, ownedByKey, player)).size();
        data.set(event.card, libraryKey, librarySize);
        LOG.info("added {} to library", event.card);
    }

}
