package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealHandler extends GameEventHandler<HealEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(HealHandler.class);

    @Override
    public void handle(HealEvent event) {
        LOG.debug("Healing {} health of {}", event.heal, inspect(event.target));
        int remainingHeal = event.heal;
        Integer oldBonusDamaged = data.getComponent(event.target, Components.Stats.BONUS_DAMAGED);
        if (oldBonusDamaged != null) {
            int newBonusDamaged = oldBonusDamaged - remainingHeal;
            LOG.debug("Changing bonus damaged of {} from {} to {}", inspect(event.target), oldBonusDamaged, newBonusDamaged);
            if (newBonusDamaged > 0) {
                data.setComponent(event.target, Components.Stats.BONUS_DAMAGED, newBonusDamaged);
                remainingHeal = 0;
            } else {
                data.removeComponent(event.target, Components.Stats.BONUS_DAMAGED);
                remainingHeal -= oldBonusDamaged;
            }
        }
        if (remainingHeal > 0) {
            Integer oldDamaged = data.getComponent(event.target, Components.Stats.DAMAGED);
            if (oldDamaged != null) {
                int newDamaged = oldDamaged - remainingHeal;
                LOG.debug("Changing damaged of {} from {} to {}", inspect(event.target), oldDamaged, newDamaged);
                if (newDamaged > 0) {
                    data.setComponent(event.target, Components.Stats.DAMAGED, newDamaged);
                } else {
                    data.removeComponent(event.target, Components.Stats.DAMAGED);
                }
            }
        }
    }
}
