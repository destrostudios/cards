package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class DrawCardEventHandler implements EventHandler<DrawCardEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int libraryKey = Components.LIBRARY, ownedByKey = Components.OWNED_BY;

    public DrawCardEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(DrawCardEvent event) {
        IntArrayList library = data.entities(libraryKey, entity -> data.hasValue(entity, ownedByKey, event.player));
        if(library.size() != 0) {
            int card = library.get(0);
            for (int i = 1; i < library.size(); i++) {
                int candidate = library.get(i);
                if(data.get(candidate, libraryKey) > data.get(card, libraryKey)) {
                    card = candidate;
                }
            }
            event.card = card;
            log.info("player {} is drawing card {}", event.player, event.card);
            events.response(new RemoveCardFromLibraryEvent(event.card));
            events.response(new AddCardToHandEvent(event.card));
        } else {
            //fatigue
            log.info("player {} tried to draw a card but has none left", event.player);
            event.cancel();
        }
    }

}
