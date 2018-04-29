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
public class AddCardToHandEventHandler implements EventHandler<AddCardToHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToHandEventHandler.class);

    private final EntityData data;
    private final EventQueue events;
    private final int handKey = Components.HAND, ownedByKey = Components.OWNED_BY;

    public AddCardToHandEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(AddCardToHandEvent event) {
        int player = data.get(event.card, ownedByKey);
        int handSize = data.entities(handKey, entity -> data.hasValue(entity, ownedByKey, player)).size();
        data.set(event.card, handKey, handSize);
        LOG.info("added {} to hand", event.card);
    }

}
