package com.destrostudios.cards.shared.rules.game.phases.attack;

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
public class StartAttackPhaseHandler extends GameEventHandler<StartAttackPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartAttackPhaseHandler.class);

    @Override
    public void handle(StartAttackPhaseEvent event) {
        data.setComponent(event.player, Components.Game.TURN_PHASE, TurnPhase.ATTACK);
        LOG.info("attack phase of {} started.", event.player);
    }
}
