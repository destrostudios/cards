package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class AddCardToLibraryHandler implements GameEventHandler<AddCardToLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToLibraryHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, AddCardToLibraryEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int librarySize = data.entities(Components.LIBRARY, entity -> data.hasComponentValue(entity, Components.OWNED_BY, player)).size();
        data.setComponent(event.card, Components.LIBRARY, librarySize);
        LOG.info("added {} to library", event.card);
    }

}
