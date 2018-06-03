package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class BattlesOnBlockPhaseEndHandler extends GameEventHandler<EndBlockPhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BattlesOnBlockPhaseEndHandler.class);

    @Override
    public void handle(EndBlockPhaseEvent event) {
        for (int attacker : data.query(Components.DECLARED_ATTACK).list(hasComponentValue(Components.OWNED_BY, event.player))) {
            List<Integer> blockers = data.query(Components.DECLARED_BLOCK).list(hasComponentValue(Components.DECLARED_BLOCK, attacker));
            if (blockers.isEmpty()) {
                int target = data.getComponent(attacker, Components.DECLARED_ATTACK);
                LOG.info("{} is unblocked, attacking {}", attacker, target);
                events.fireChainEvent(new BattleEvent(attacker, target));
            } else {
                LOG.info("{} is blocked by {}", attacker, blockers);
                for (int blocker : blockers) {
                    events.fireChainEvent(new BattleEvent(attacker, blocker));
                    data.removeComponent(blocker, Components.DECLARED_BLOCK);
                }
            }
            data.removeComponent(attacker, Components.DECLARED_ATTACK);
        }
    }
}
