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
public class RemoveCardFromHandEventHandler implements EventHandler<RemoveCardFromHandEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int handKey = Components.HAND, ownedByKey = Components.OWNED_BY;

    public RemoveCardFromHandEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(RemoveCardFromHandEvent event) {
        int player = data.get(event.card, ownedByKey);
        int handIndex = data.get(event.card, handKey);
        for (int handCard : data.entities(handKey, x -> data.hasValue(x, ownedByKey, player), x -> data.get(x, handKey) > handIndex)) {
            data.set(handCard, handKey, data.get(handCard, handKey) - 1);
        }
        data.remove(event.card, handKey);
        log.info("removed {} from hand", event.card);
    }

}
