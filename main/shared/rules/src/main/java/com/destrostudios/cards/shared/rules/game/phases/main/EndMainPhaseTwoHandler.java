package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseTwoHandler extends GameEventHandler<EndMainPhaseTwoEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndMainPhaseTwoHandler.class);

    @Override
    public void handle(EndMainPhaseTwoEvent event) {
        data.removeComponent(event.player, Components.Game.TURN_PHASE);
        LOG.debug("main phase 2 of {} ended.", event.player);
        int nextPlayer = data.getComponent(event.player, Components.NEXT_PLAYER);
        events.fireChainEvent(new StartMainPhaseOneEvent(nextPlayer));
    }
}
