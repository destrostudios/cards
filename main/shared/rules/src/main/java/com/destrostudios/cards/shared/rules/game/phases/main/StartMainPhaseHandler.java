package com.destrostudios.cards.shared.rules.game.phases.main;

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
public class StartMainPhaseHandler extends GameEventHandler<StartMainPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartMainPhaseHandler.class);

    @Override
    public void handle(StartMainPhaseEvent event) {
        data.setComponent(event.player, Components.Game.TURN_PHASE, TurnPhase.MAIN);
        LOG.info("main phase of {} started.", event.player);
    }
}
