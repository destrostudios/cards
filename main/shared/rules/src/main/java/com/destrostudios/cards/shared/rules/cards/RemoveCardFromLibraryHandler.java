package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Philipp
 */
public class RemoveCardFromLibraryHandler implements GameEventHandler<RemoveCardFromLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromLibraryHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, RemoveCardFromLibraryEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int libraryIndex = data.getComponent(event.card, Components.LIBRARY);
        for (int libraryCard : data.entities(Components.LIBRARY,
                x -> data.hasComponentValue(x, Components.OWNED_BY, player),
                x -> data.getComponent(x, Components.LIBRARY) > libraryIndex)) {

            data.setComponent(libraryCard, Components.LIBRARY, data.getComponent(libraryCard, Components.LIBRARY) - 1);
        }
        data.removeComponent(event.card, Components.LIBRARY);
        LOG.info("removed {} from library", event.card);
    }

}
