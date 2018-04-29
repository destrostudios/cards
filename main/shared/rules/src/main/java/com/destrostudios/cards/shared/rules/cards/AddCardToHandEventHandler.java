package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class AddCardToHandEventHandler implements EventHandler<AddCardToHandEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int handKey = Components.HAND, ownedByKey = Components.OWNED_BY;

    public AddCardToHandEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(AddCardToHandEvent event) {
        int player = data.get(event.card, ownedByKey);
        int handSize = data.entities(handKey, entity -> data.hasValue(entity, ownedByKey, player)).size();
        data.set(event.card, handKey, handSize);
        log.info("added {} to hand", event.card);
    }

}
