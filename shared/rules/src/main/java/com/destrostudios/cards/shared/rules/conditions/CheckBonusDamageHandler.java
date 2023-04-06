package com.destrostudios.cards.shared.rules.conditions;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckBonusDamageHandler extends GameEventHandler<ConditionsAffectedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ConditionsAffectedEvent.class);

    @Override
    public void handle(ConditionsAffectedEvent event, NetworkRandom random) {
        LOG.info("Checking bonus health");
        for (int entity : data.query(Components.Stats.BONUS_DAMAGED).list()) {
            int bonusDamage = data.getComponent(entity, Components.Stats.BONUS_DAMAGED);
            int bonusHealth = StatsUtil.getBonusHealth(data, entity);
            if (bonusHealth < bonusDamage) {
                if (bonusHealth > 0) {
                    data.setComponent(entity, Components.Stats.BONUS_DAMAGED, bonusHealth);
                } else {
                    data.removeComponent(entity, Components.Stats.BONUS_DAMAGED);
                }
            }
        }
    }
}
