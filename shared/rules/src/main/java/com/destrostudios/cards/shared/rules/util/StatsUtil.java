package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class StatsUtil {

    private static final BuffUtil.BuffModifier ATTACK_BUFF_MODIFIER = new BuffUtil.SimpleBuffModifier(Components.Stats.BONUS_ATTACK, Components.Stats.SET_ATTACK);
    private static final BuffUtil.BuffModifier HEALTH_BUFF_MODIFIER = new BuffUtil.SimpleBuffModifier(Components.Stats.BONUS_HEALTH, Components.Stats.SET_HEALTH);

    public static Integer getEffectiveAttack(EntityData data, int entity) {
        BuffUtil.BuffCalculationResult result = calculateAttack(data, entity);
        return ((result != null) ? result.getEffectiveValue() : null);
    }

    private static BuffUtil.BuffCalculationResult calculateAttack(EntityData data, int entity) {
        return BuffUtil.calculateWithBuffs(data, entity, Components.Stats.ATTACK, ATTACK_BUFF_MODIFIER);
    }

    public static Integer getEffectiveHealth(EntityData data, int entity) {
        BuffUtil.BuffCalculationResult result = calculateHealth(data, entity);
        if (result != null) {
            int health = result.getEffectiveValue();
            Integer damage = data.getComponent(entity, Components.Stats.DAMAGED);
            if (damage != null) {
                health -= damage;
            }
            Integer bonusDamage = data.getComponent(entity, Components.Stats.BONUS_DAMAGED);
            if (bonusDamage != null) {
                health -= bonusDamage;
            }
            return health;
        }
        return null;
    }

    public static Integer getBonusHealth(EntityData data, int entity) {
        BuffUtil.BuffCalculationResult result = calculateHealth(data, entity);
        return ((result != null) ? result.getBonusValue() : null);
    }

    private static BuffUtil.BuffCalculationResult calculateHealth(EntityData data, int entity) {
        return BuffUtil.calculateWithBuffs(data, entity, Components.Stats.HEALTH, HEALTH_BUFF_MODIFIER);
    }

    public static boolean isDamaged(EntityData data, int entity) {
        return data.hasComponent(entity, Components.Stats.DAMAGED) || data.hasComponent(entity, Components.Stats.BONUS_DAMAGED);
    }
}
