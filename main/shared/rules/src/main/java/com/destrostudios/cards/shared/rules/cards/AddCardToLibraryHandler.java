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
public class AddCardToLibraryHandler implements EventHandler<AddCardToLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToLibraryHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public AddCardToLibraryHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(AddCardToLibraryEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int librarySize = data.entities(Components.LIBRARY, entity -> data.hasComponentValue(entity, Components.OWNED_BY, player)).size();
        data.setComponent(event.card, Components.LIBRARY, librarySize);
        LOG.info("added {} to library", event.card);
    }

}
