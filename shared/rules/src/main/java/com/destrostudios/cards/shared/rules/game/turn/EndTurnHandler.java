package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndTurnHandler.class);

    @Override
    public void handle(GameContext context, EndTurnEvent event) {
        EntityData data = context.getData();
        LOG.debug("Ending turn of player {}", inspect(data, event.player));
        data.removeComponent(event.player, Components.Player.ACTIVE_PLAYER);
        int nextPlayer = data.getComponent(event.player, Components.NEXT_PLAYER);
        context.getEvents().fire(new StartTurnEvent(nextPlayer));
    }
}
