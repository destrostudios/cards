package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartBattleEventHandler implements EventHandler<StartBattlePhaseEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartBattleEventHandler.class);
    
    private final EntityData data;
    private final EventQueue events;

    public StartBattleEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(StartBattlePhaseEvent event) {
        for (int blocker : data.entities(Components.DECLARED_BLOCK, x -> data.hasValue(x, Components.OWNED_BY, event.player))) {
            int blocked = data.get(blocker, Components.DECLARED_BLOCK);
            LOG.info("blocking {} with {}", blocked, blocker);
            events.response(new BattleEvent(blocker, blocked));
            data.remove(blocker, Components.DECLARED_BLOCK);
        }
        
        for (int attacker : data.entities(Components.DECLARED_ATTACK, x -> data.hasValue(data.get(x, Components.DECLARED_ATTACK), Components.OWNED_BY, event.player))) {
            int attacked = data.get(attacker, Components.DECLARED_ATTACK);
            LOG.info("attacking {} with {}", attacked, attacker);
            events.response(new BattleEvent(attacker, attacked));
            data.remove(attacker, Components.DECLARED_ATTACK);
        }
    }

}
