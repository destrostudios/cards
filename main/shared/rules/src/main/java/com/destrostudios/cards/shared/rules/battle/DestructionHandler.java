package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToGraveyardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestructionHandler extends GameEventHandler<DestructionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DestructionHandler.class);

    @Override
    public void handle(DestructionEvent event) {
        LOG.info("{} destroyed", event.target);
        if (data.hasComponent(event.target, Components.NEXT_PLAYER)) {
            // TODO: Game over
            LOG.info("Game over");
        }
        else {
            events.fireSubEvent(new AddCardToGraveyardEvent(event.target));
        }
    }
}
