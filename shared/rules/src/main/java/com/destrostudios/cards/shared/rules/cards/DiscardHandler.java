package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToGraveyardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardHandler extends GameEventHandler<DiscardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DiscardHandler.class);

    @Override
    public void handle(GameContext context, DiscardEvent event) {
        EntityData data = context.getData();
        LOG.debug("Discarding card {}", inspect(data, event.card));
        context.getEvents().fire(new MoveToGraveyardEvent(event.card));
    }
}
