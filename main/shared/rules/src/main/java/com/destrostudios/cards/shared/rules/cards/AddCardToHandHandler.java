package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class AddCardToHandHandler extends GameEventHandler<AddCardToHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToHandHandler.class);

    @Override
    public void handle(AddCardToHandEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int handSize = data.entities(Components.HAND_CARDS, entity -> data.hasComponentValue(entity, Components.OWNED_BY, player)).size();
        data.setComponent(event.card, Components.HAND_CARDS, handSize);
        LOG.info("added {} to hand", event.card);
    }

}
