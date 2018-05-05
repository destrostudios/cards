package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class AddCardToLibraryHandler extends GameEventHandler<AddCardToLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToLibraryHandler.class);

    @Override
    public void handle(AddCardToLibraryEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int librarySize = data.entities(Components.LIBRARY, entity -> data.hasComponentValue(entity, Components.OWNED_BY, player)).size();
        data.setComponent(event.card, Components.LIBRARY, librarySize);
        LOG.info("added {} to library", event.card);
    }

}
