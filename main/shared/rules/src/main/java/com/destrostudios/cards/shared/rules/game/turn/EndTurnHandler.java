package com.destrostudios.cards.shared.rules.game.turn;

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
        data.removeComponent(event.player, Components.Game.ACTIVE_PLAYER);
        LOG.debug("turn of {} ended.", event.player);
        int nextPlayer = data.getComponent(event.player, Components.NEXT_PLAYER);
        events.fire(new StartTurnEvent(nextPlayer));
    }
}
