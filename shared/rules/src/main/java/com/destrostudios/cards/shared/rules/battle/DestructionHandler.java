package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToGraveyardEvent;
import com.destrostudios.cards.shared.rules.game.GameOverEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestructionHandler extends GameEventHandler<DestructionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DestructionHandler.class);

    @Override
    public void handle(GameContext context, DestructionEvent event) {
        EntityData data = context.getData();
        if (data.hasComponent(event.target, Components.BOARD)) {
            LOG.debug("Destroying {}", inspect(data, event.target));
            Integer nextPlayer = data.getComponent(event.target, Components.NEXT_PLAYER);
            if (nextPlayer != null) {
                context.getEvents().fire(new GameOverEvent(nextPlayer));
            } else {
                context.getEvents().fire(new MoveToGraveyardEvent(event.target));
            }
        } else {
            LOG.debug("{} is not on board, preventing destruction.", inspect(data, event.target));
            event.cancel();
        }
    }
}
