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
public class RemoveCardFromHandHandler implements GameEventHandler<RemoveCardFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromHandHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, RemoveCardFromHandEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int handIndex = data.getComponent(event.card, Components.HAND_CARDS);
        for (int handCard : data.entities(Components.HAND_CARDS,
                x -> data.hasComponentValue(x, Components.OWNED_BY, player),
                x -> data.getComponent(x, Components.HAND_CARDS) > handIndex)) {

            data.setComponent(handCard, Components.HAND_CARDS, data.getComponent(handCard, Components.HAND_CARDS) - 1);
        }
        data.removeComponent(event.card, Components.HAND_CARDS);
        LOG.info("removed {} from hand", event.card);
    }

}
