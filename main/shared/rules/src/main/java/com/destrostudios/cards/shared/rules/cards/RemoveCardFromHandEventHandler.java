package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.ComponentValue;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class RemoveCardFromHandEventHandler implements EventHandler<RemoveCardFromHandEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int handKey, ownedByKey;

    public RemoveCardFromHandEventHandler(EntityData data, EventQueue events, Logger log, int handKey, int ownedByKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.handKey = handKey;
        this.ownedByKey = ownedByKey;
    }

    @Override
    public void onEvent(RemoveCardFromHandEvent event) {
        int player = data.get(event.card, ownedByKey);
        int handIndex = data.get(event.card, handKey);
        for (ComponentValue componentValue : data.entityComponentValues(handKey, x -> data.hasValue(x.getEntity(), ownedByKey, player), x -> x.getComponentValue() > handIndex)) {
            data.set(componentValue.getEntity(), handKey, componentValue.getComponentValue() - 1);
        }
        data.remove(event.card, handKey);
        log.info("removed {} from hand", event.card);
    }

}
