package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndTurnHandler.class);

    @Override
    public void handle(EndTurnEvent event) {
        data.removeComponent(event.player, Components.ACTIVE_PLAYER);
        Integer nextPlayer = data.getComponent(event.player, Components.NEXT_PLAYER);
        data.setComponent(nextPlayer, Components.ACTIVE_PLAYER);
        LOG.info("Active player changed from {} to {}.", event.player, nextPlayer);
    }
}
