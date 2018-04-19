package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class AddCardToLibraryEventHandler implements EventHandler<AddCardToLibraryEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int libraryKey, ownedByKey;

    public AddCardToLibraryEventHandler(EntityData data, EventQueue events, Logger log, int libraryKey, int ownedByKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.libraryKey = libraryKey;
        this.ownedByKey = ownedByKey;
    }

    @Override
    public void onEvent(AddCardToLibraryEvent event) {
        int player = data.get(event.card, ownedByKey);
        int librarySize = data.entitiesWithComponent(libraryKey, entity -> data.hasValue(entity, ownedByKey, player)).size();
        data.set(event.card, libraryKey, librarySize);
        log.info("added {} to library", event.card);
    }

}
