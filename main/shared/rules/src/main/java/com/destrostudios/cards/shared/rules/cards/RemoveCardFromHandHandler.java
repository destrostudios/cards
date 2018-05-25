package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class RemoveCardFromHandHandler extends GameEventHandler<RemoveCardFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromHandHandler.class);

    @Override
    public void handle(RemoveCardFromHandEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int handIndex = data.getComponent(event.card, Components.HAND_CARDS);
        for (int handCard : data.query(Components.HAND_CARDS).list(
                        x -> data.hasComponentValue(x, Components.OWNED_BY, player)
                        && data.getComponent(x, Components.HAND_CARDS) > handIndex)) {
            
            data.setComponent(handCard, Components.HAND_CARDS, data.getComponent(handCard, Components.HAND_CARDS) - 1);
        }
        data.removeComponent(event.card, Components.HAND_CARDS);
        LOG.info("removed {} from hand", event.card);
    }

}
