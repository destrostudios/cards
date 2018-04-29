package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
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
    private final int declaredBlockKey = Components.DECLARED_BLOCK, declaredAttackKey = Components.DECLARED_ATTACK, ownedByKey = Components.OWNED_BY;

    public BattleEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(StartBattlePhaseEvent event) {
        for (int blocker : data.entities(declaredBlockKey, x -> data.hasValue(x, ownedByKey, event.player))) {
            int blocked = data.get(blocker, declaredBlockKey);
            log.info("blocking {} with {}", blocked, blocker);
            events.response(new AttackEvent(blocker, blocked));
            data.remove(blocker, declaredBlockKey);
        }
        
        for (int attacker : data.entities(declaredAttackKey, x -> data.hasValue(data.get(x, declaredAttackKey), ownedByKey, event.player))) {
            int attacked = data.get(attacker, declaredAttackKey);
            log.info("attacking {} with {}", attacked, attacker);
            events.response(new AttackEvent(attacker, attacked));
            data.remove(attacker, declaredAttackKey);
        }
    }

}
