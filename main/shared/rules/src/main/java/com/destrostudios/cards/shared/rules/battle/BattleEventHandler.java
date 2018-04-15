package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
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
    private final int declaredBlockKey, declaredAttackKey;

    public BattleEventHandler(EntityData data, EventQueue events, Logger log, int declaredBlockKey, int declaredAttackKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.declaredBlockKey = declaredBlockKey;
        this.declaredAttackKey = declaredAttackKey;
    }

    @Override
    public void onEvent(StartBattlePhaseEvent event) {
        data.foreachEntityWithComponent(declaredBlockKey, (blocker, blocked) -> {
            log.info("blocking {} with {}", blocked, blocker);
            events.response(new AttackEvent(blocker, blocked));
        });
        data.foreachEntityWithComponent(declaredAttackKey, (attacker, attacked) -> {
            log.info("attacking {} with {}", attacked, attacker);
            events.response(new AttackEvent(attacker, attacked));
        });
    }

}
