package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageHandler extends GameEventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    @Override
    public void handle(GameContext context, DamageEvent event) {
        boolean damaged = false;
        EntityData data = context.getData();
        LOG.debug("Dealing {} damage to {}", event.damage, inspect(data, event.target));
        if (data.getComponentOrElse(event.target, Components.Ability.DIVINE_SHIELD, false)) {
            LOG.debug("Removing divine shield from {}", inspect(data, event.target));
            data.setComponent(event.target, Components.Ability.DIVINE_SHIELD, false);
        } else {
            int remainingDamage = event.damage;
            int bonusHealth = StatsUtil.getBonusHealth(data, event.target);
            if (bonusHealth > 0) {
                int oldBonusDamaged = data.getComponentOrElse(event.target, Components.Stats.BONUS_DAMAGED, 0);
                int bonusDamageDealt = Math.min(remainingDamage, bonusHealth - oldBonusDamaged);
                if (bonusDamageDealt > 0) {
                    int newBonusDamaged = oldBonusDamaged + bonusDamageDealt;
                    data.setComponent(event.target, Components.Stats.BONUS_DAMAGED, newBonusDamaged);
                    LOG.debug("Changing bonus damaged of {} from {} to {}", inspect(data, event.target), oldBonusDamaged, newBonusDamaged);
                    remainingDamage -= bonusDamageDealt;
                    damaged = true;
                }
            }
            if (remainingDamage > 0) {
                int oldDamaged = data.getComponentOrElse(event.target, Components.Stats.DAMAGED, 0);
                int newDamaged = oldDamaged + remainingDamage;
                LOG.debug("Changing damaged of {} from {} to {}", inspect(data, event.target), oldDamaged, newDamaged);
                data.setComponent(event.target, Components.Stats.DAMAGED, newDamaged);
                damaged = true;
            }
        }
        if (!damaged) {
            event.cancel();
        }
    }
}
