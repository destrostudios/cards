package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.phases.attack.StartAttackPhaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndMainPhaseHandler extends GameEventHandler<EndMainPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndMainPhaseHandler.class);

    @Override
    public void handle(EndMainPhaseEvent event) {
        data.removeComponent(event.player, Components.Game.TURN_PHASE);
        LOG.debug("main phase of {} ended.", event.player);
        int nextPlayer = data.getComponent(event.player, Components.NEXT_PLAYER);
        events.fireChainEvent(new StartAttackPhaseEvent(nextPlayer));
    }
}
