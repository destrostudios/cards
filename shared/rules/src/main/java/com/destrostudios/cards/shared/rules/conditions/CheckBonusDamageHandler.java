package com.destrostudios.cards.shared.rules.conditions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckBonusDamageHandler extends GameEventHandler<ConditionsAffectedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CheckBonusDamageHandler.class);

    @Override
    public void handle(GameContext context, ConditionsAffectedEvent event) {
        EntityData data = context.getData();
        for (int entity : data.list(Components.Stats.BONUS_DAMAGED)) {
            int bonusDamage = data.getComponent(entity, Components.Stats.BONUS_DAMAGED);
            int bonusHealth = StatsUtil.getBonusHealth(data, entity);
            if (bonusHealth < bonusDamage) {
                LOG.debug("Changing bonus damaged of {} to {}", inspect(data, entity), bonusHealth);
                if (bonusHealth > 0) {
                    data.setComponent(entity, Components.Stats.BONUS_DAMAGED, bonusHealth);
                } else {
                    data.removeComponent(entity, Components.Stats.BONUS_DAMAGED);
                }
            }
        }
    }
}
