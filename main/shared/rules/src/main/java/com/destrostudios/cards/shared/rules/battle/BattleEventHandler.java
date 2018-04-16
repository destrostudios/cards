package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.EntityValue;
import com.destrostudios.cards.shared.entities.Query;
import com.destrostudios.cards.shared.entities.QueryBuilder;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class BattleEventHandler implements EventHandler<StartBattlePhaseEvent> {

    private final Query declaredBlockers, declaredAttackers;
    private final EntityData data;
    private final EventQueue events;
    private final Logger log;

    public BattleEventHandler(EntityData data, EventQueue events, Logger log, int declaredBlockKey, int declaredAttackKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.declaredBlockers = new QueryBuilder().from(declaredBlockKey).build();
        this.declaredAttackers = new QueryBuilder().from(declaredAttackKey).build();
    }

    @Override
    public void onEvent(StartBattlePhaseEvent event) {
        for (EntityValue entityValue : declaredBlockers.fetch(data)) {
            int blocker = entityValue.getEntity();
            int blocked = entityValue.getValue();
            log.info("blocking {} with {}", blocked, blocker);
            events.response(new AttackEvent(blocker, blocked));
        }
        
        for (EntityValue entityValue : declaredAttackers.fetch(data)) {
            int attacker = entityValue.getEntity();
            int attacked = entityValue.getValue();
            log.info("attacking {} with {}", attacked, attacker);
            events.response(new AttackEvent(attacker, attacked));
        }
    }

}
