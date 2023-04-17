package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartTurnHandler extends GameEventHandler<StartTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartTurnHandler.class);

    @Override
    public void handle(StartTurnEvent event) {
        LOG.debug("Starting turn of {}", inspect(event.player));
        data.setComponent(event.player, Components.Player.ACTIVE_PLAYER);
    }
}
