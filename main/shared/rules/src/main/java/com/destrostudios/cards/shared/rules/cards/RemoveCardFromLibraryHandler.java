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
public class RemoveCardFromLibraryHandler implements EventHandler<RemoveCardFromLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromLibraryHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public RemoveCardFromLibraryHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(RemoveCardFromLibraryEvent event) {
        int player = data.get(event.card, Components.OWNED_BY);
        int libraryIndex = data.get(event.card, Components.LIBRARY);
        for (int libraryCard : data.entities(Components.LIBRARY, x -> data.hasValue(x, Components.OWNED_BY, player), x -> data.get(x, Components.LIBRARY) > libraryIndex)) {
            data.set(libraryCard, Components.LIBRARY, data.get(libraryCard, Components.LIBRARY) - 1);
        }
        data.remove(event.card, Components.LIBRARY);
        LOG.info("removed {} from library", event.card);
    }

}
