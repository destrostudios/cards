package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartTurnHandler extends GameEventHandler<StartTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartTurnHandler.class);

    @Override
    public void handle(StartTurnEvent event, NetworkRandom random) {
        data.setComponent(event.player, Components.Game.ACTIVE_PLAYER);
        LOG.info("turn of {} started.", event.player);
    }
}