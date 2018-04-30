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
public class RemoveCardFromHandEventHandler implements EventHandler<RemoveCardFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromHandEventHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public RemoveCardFromHandEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(RemoveCardFromHandEvent event) {
        int player = data.get(event.card, Components.OWNED_BY);
        int handIndex = data.get(event.card, Components.HAND_CARDS);
        for (int handCard : data.entities(Components.HAND_CARDS, x -> data.hasValue(x, Components.OWNED_BY, player), x -> data.get(x, Components.HAND_CARDS) > handIndex)) {
            data.set(handCard, Components.HAND_CARDS, data.get(handCard, Components.HAND_CARDS) - 1);
        }
        data.remove(event.card, Components.HAND_CARDS);
        LOG.info("removed {} from hand", event.card);
    }

}
