package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleHandler extends GameEventHandler<BattleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BattleHandler.class);

    @Override
    public void handle(BattleEvent event) {
        LOG.debug("{} is attacking {}", inspect(event.source), inspect(event.target));
        Integer damageToTarget = StatsUtil.getEffectiveAttack(data, event.source);
        Integer damageToSource = StatsUtil.getEffectiveAttack(data, event.target);
        tryDealDamage(event.target, damageToTarget);
        tryDealDamage(event.source, damageToSource);
    }

    private void tryDealDamage(int target, Integer damage) {
        if ((damage != null) && (damage > 0)) {
            events.fire(new DamageEvent(target, damage));
        }
    }
}
