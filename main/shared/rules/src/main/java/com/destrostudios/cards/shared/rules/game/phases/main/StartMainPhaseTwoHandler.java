package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartMainPhaseTwoHandler extends GameEventHandler<StartMainPhaseTwoEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartMainPhaseTwoHandler.class);

    @Override
    public void handle(StartMainPhaseTwoEvent event) {
        data.setComponent(event.player, Components.Game.TURN_PHASE, TurnPhase.MAIN_TWO);
        LOG.info("main phase 2 of {} started.", event.player);
    }
}
