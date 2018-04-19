package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.ComponentValue;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class BattleEventHandler implements EventHandler<StartBattlePhaseEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int declaredBlockKey, declaredAttackKey, ownedByKey;

    public BattleEventHandler(EntityData data, EventQueue events, Logger log, int declaredBlockKey, int declaredAttackKey, int ownedByKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.declaredAttackKey = declaredAttackKey;
        this.declaredBlockKey = declaredBlockKey;
        this.ownedByKey = ownedByKey;
    }

    @Override
    public void onEvent(StartBattlePhaseEvent event) {
        for (ComponentValue entityValue : data.entityComponentValues(declaredBlockKey, x -> data.hasValue(x.getEntity(), ownedByKey, event.player))) {
            int blocker = entityValue.getEntity();
            int blocked = entityValue.getComponentValue();
            log.info("blocking {} with {}", blocked, blocker);
            events.response(new AttackEvent(blocker, blocked));
            data.remove(blocker, declaredBlockKey);
        }
        
        for (ComponentValue entityValue : data.entityComponentValues(declaredAttackKey, x -> data.hasValue(x.getComponentValue(), ownedByKey, event.player))) {
            int attacker = entityValue.getEntity();
            int attacked = entityValue.getComponentValue();
            log.info("attacking {} with {}", attacked, attacker);
            events.response(new AttackEvent(attacker, attacked));
            data.remove(attacker, declaredAttackKey);
        }
    }

}
