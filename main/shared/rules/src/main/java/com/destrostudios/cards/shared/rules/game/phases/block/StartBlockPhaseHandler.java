package com.destrostudios.cards.shared.rules.game.phases.block;

import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartBlockPhaseHandler extends GameEventHandler<StartBlockPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartBlockPhaseHandler.class);

    @Override
    public void handle(StartBlockPhaseEvent event) {
        data.setComponent(event.player, Components.Game.TURN_PHASE, TurnPhase.BLOCK);
        LOG.info("block phase of {} started.", event.player);
    }
}
