package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartMainPhaseOneHandler extends GameEventHandler<StartMainPhaseOneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartMainPhaseOneHandler.class);

    @Override
    public void handle(StartMainPhaseOneEvent event) {
        data.setComponent(event.player, Components.Game.TURN_PHASE, TurnPhase.MAIN_ONE);
        LOG.info("main phase 1 of {} started.", event.player);
    }
}
