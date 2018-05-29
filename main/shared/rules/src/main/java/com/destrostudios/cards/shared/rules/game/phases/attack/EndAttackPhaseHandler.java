package com.destrostudios.cards.shared.rules.game.phases.attack;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.phases.block.StartBlockPhaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EndAttackPhaseHandler extends GameEventHandler<EndAttackPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndAttackPhaseHandler.class);

    @Override
    public void handle(EndAttackPhaseEvent event) {
        data.removeComponent(event.player, Components.Game.TURN_PHASE);
        LOG.debug("attack phase of {} ended.", event.player);
        int nextPlayer = data.getComponent(event.player, Components.NEXT_PLAYER);
        events.fireChainEvent(new StartBlockPhaseEvent(nextPlayer));

    }
}
