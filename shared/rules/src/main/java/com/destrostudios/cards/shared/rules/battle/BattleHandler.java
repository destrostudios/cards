package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleHandler extends GameEventHandler<BattleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BattleHandler.class);

    @Override
    public void handle(GameContext context, BattleEvent event) {
        EntityData data = context.getData();
        LOG.debug("{} is attacking {}", inspect(data, event.source), inspect(data, event.target));
        Integer damageToTarget = StatsUtil.getEffectiveAttack(data, event.source);
        Integer damageToSource = StatsUtil.getEffectiveAttack(data, event.target);
        tryDealDamage(context, event.target, damageToTarget);
        tryDealDamage(context, event.source, damageToSource);
    }

    private void tryDealDamage(GameContext context, int target, Integer damage) {
        if ((damage != null) && (damage > 0)) {
            context.getEvents().fire(new DamageEvent(target, damage));
        }
    }
}
