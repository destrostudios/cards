package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageHandler extends GameEventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    @Override
    public void handle(DamageEvent event, NetworkRandom random) {
        LOG.info("dealing {} damage to {}", event.damage, event.target);
        if (data.getOptionalComponent(event.target, Components.Ability.DIVINE_SHIELD).orElse(false)) {
            LOG.info("remove divine shield from {}", event.target);
            data.setComponent(event.target, Components.Ability.DIVINE_SHIELD, false);
        } else {
            int remainingDamage = event.damage;
            int bonusHealth = StatsUtil.getBonusHealth(data, event.target);
            if (bonusHealth > 0) {
                int oldBonusDamaged = data.getOptionalComponent(event.target, Components.Stats.BONUS_DAMAGED).orElse(0);
                int bonusDamageDealt = Math.min(remainingDamage, bonusHealth - oldBonusDamaged);
                int newBonusDamaged = oldBonusDamaged + bonusDamageDealt;
                data.setComponent(event.target, Components.Stats.BONUS_DAMAGED, newBonusDamaged);
                LOG.info("changing bonus damaged of {} from {} to {}", event.target, oldBonusDamaged, newBonusDamaged);
                remainingDamage -= bonusDamageDealt;
            }
            if (remainingDamage > 0) {
                int oldDamaged = data.getOptionalComponent(event.target, Components.Stats.DAMAGED).orElse(0);
                int newDamaged = oldDamaged + remainingDamage;
                LOG.info("changing damaged of {} from {} to {}", event.target, oldDamaged, newDamaged);
                data.setComponent(event.target, Components.Stats.DAMAGED, newDamaged);
            }
        }
    }
}
