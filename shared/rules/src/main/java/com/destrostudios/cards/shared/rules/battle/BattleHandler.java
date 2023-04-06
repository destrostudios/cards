package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleHandler extends GameEventHandler<BattleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BattleHandler.class);

    @Override
    public void handle(BattleEvent event, NetworkRandom random) {
        LOG.info(inspect(event.source) + " is attacking " + inspect(event.target));
        Integer damageToTarget = StatsUtil.getEffectiveAttack(data, event.source);
        Integer damageToSource = StatsUtil.getEffectiveAttack(data, event.target);
        tryDealDamage(event.target, damageToTarget, random);
        tryDealDamage(event.source, damageToSource, random);
    }

    private void tryDealDamage(int target, Integer damage, NetworkRandom random) {
        if ((damage != null) && (damage > 0)) {
            events.fire(new DamageEvent(target, damage), random);
        }
    }
}
