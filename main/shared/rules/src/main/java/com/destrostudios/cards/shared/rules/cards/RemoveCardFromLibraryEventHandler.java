package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.ComponentValue;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class RemoveCardFromLibraryEventHandler implements EventHandler<RemoveCardFromLibraryEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int libraryKey, ownedByKey;

    public RemoveCardFromLibraryEventHandler(EntityData data, EventQueue events, Logger log, int libraryKey, int ownedByKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.libraryKey = libraryKey;
        this.ownedByKey = ownedByKey;
    }

    @Override
    public void onEvent(RemoveCardFromLibraryEvent event) {
        int player = data.get(event.card, ownedByKey);
        int libraryIndex = data.get(event.card, libraryKey);
        for (ComponentValue componentValue : data.entityComponentValues(libraryKey, x -> data.hasValue(x.getEntity(), ownedByKey, player), x -> x.getComponentValue() > libraryIndex)) {
            data.set(componentValue.getEntity(), libraryKey, componentValue.getComponentValue() - 1);
        }
        data.remove(event.card, libraryKey);
        log.info("removed {} from library", event.card);
    }

}
