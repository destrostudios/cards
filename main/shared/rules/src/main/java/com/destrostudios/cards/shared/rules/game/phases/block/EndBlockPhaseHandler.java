package com.destrostudios.cards.shared.rules.game.phases.block;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.phases.main.StartMainPhaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndBlockPhaseHandler extends GameEventHandler<EndBlockPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndBlockPhaseHandler.class);

    @Override
    public void handle(EndBlockPhaseEvent event) {
        data.removeComponent(event.player, Components.Game.TURN_PHASE);
        LOG.debug("block phase of {} ended.", event.player);
        int nextPlayer = data.getComponent(event.player, Components.NEXT_PLAYER);
        events.fireChainEvent(new StartMainPhaseEvent(nextPlayer));
    }
}
