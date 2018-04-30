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

    public AddCardToHandEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(AddCardToHandEvent event) {
        int player = data.get(event.card, Components.OWNED_BY);
        int handSize = data.entities(Components.HAND, entity -> data.hasValue(entity, Components.OWNED_BY, player)).size();
        data.set(event.card, Components.HAND, handSize);
        LOG.info("added {} to hand", event.card);
    }

}
