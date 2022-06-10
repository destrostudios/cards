package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import lombok.AllArgsConstructor;

import java.util.List;

public class StatsUtil {

    private static final BuffModifier ATTACK_MODIFIER = new SimpleAdditionBuffModifier(Components.Stats.BONUS_ATTACK);
    private static final BuffModifier HEALTH_MODIFIER = new SimpleAdditionBuffModifier(Components.Stats.BONUS_HEALTH);

    public static Integer getEffectiveAttack(EntityData data, int entity) {
        Integer attack = data.getComponent(entity, Components.Stats.ATTACK);
        if (attack != null) {
            attack += getBonusAttack(data, entity);
        }
        return attack;
    }

    public static int getBonusAttack(EntityData data, int entity) {
        return modifyViaBuffs(data, entity, 0, ATTACK_MODIFIER);
    }

    public static Integer getEffectiveHealth(EntityData data, int entity) {
        Integer health = data.getComponent(entity, Components.Stats.HEALTH);
        if (health != null) {
            health += getBonusHealth(data, entity);
            Integer damage = data.getComponent(entity, Components.Stats.DAMAGED);
            if (damage != null) {
                health -= damage;
            }
            Integer bonusDamage = data.getComponent(entity, Components.Stats.BONUS_DAMAGED);
            if (bonusDamage != null) {
                health -= bonusDamage;
            }
        }
        return health;
    }

    public static int getBonusHealth(EntityData data, int entity) {
        return modifyViaBuffs(data, entity, 0, HEALTH_MODIFIER);
    }

    private static Integer modifyViaBuffs(EntityData data, int entity, int initialValue, BuffModifier buffModifier) {
        int value = initialValue;
        List<Integer> buffs = BuffUtil.getAffectingBuffs(data, entity);
        for (int buff : buffs) {
            value = buffModifier.getModifiedValue(data, buff, value);
        }
        return value;
    }

    @AllArgsConstructor
    private static class SimpleAdditionBuffModifier implements BuffModifier {

        private ComponentDefinition<Integer> component;

        public int getModifiedValue(EntityData data, int buff, int value) {
            Integer additionalValue = data.getComponent(buff, component);
            if (additionalValue != null) {
                return value + additionalValue;
            }
            return value;
        }
    }

    private interface BuffModifier {
        int getModifiedValue(EntityData data, int buff, int value);
    }
}
