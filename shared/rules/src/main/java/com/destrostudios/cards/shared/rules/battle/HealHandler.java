package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealHandler extends GameEventHandler<HealEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(HealHandler.class);

    @Override
    public void handle(GameContext context, HealEvent event) {
        EntityData data = context.getData();
        LOG.debug("Healing {} health of {}", event.heal, inspect(data, event.target));
        int remainingHeal = event.heal;
        Integer oldBonusDamaged = data.getComponent(event.target, Components.Stats.BONUS_DAMAGED);
        if (oldBonusDamaged != null) {
            int newBonusDamaged = oldBonusDamaged - remainingHeal;
            LOG.debug("Changing bonus damaged of {} from {} to {}", inspect(data, event.target), oldBonusDamaged, newBonusDamaged);
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
                LOG.debug("Changing damaged of {} from {} to {}", inspect(data, event.target), oldDamaged, newDamaged);
                if (newDamaged > 0) {
                    data.setComponent(event.target, Components.Stats.DAMAGED, newDamaged);
                } else {
                    data.removeComponent(event.target, Components.Stats.DAMAGED);
                }
            }
        }
    }
}
