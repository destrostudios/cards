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
public class AddCardToHandHandler implements GameEventHandler<AddCardToHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToHandHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, AddCardToHandEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int handSize = data.entities(Components.HAND_CARDS, entity -> data.hasComponentValue(entity, Components.OWNED_BY, player)).size();
        data.setComponent(event.card, Components.HAND_CARDS, handSize);
        LOG.info("added {} to hand", event.card);
    }

}
