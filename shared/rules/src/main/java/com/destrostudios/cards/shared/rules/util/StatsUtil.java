package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class StatsUtil {

    private static final BuffUtil.BuffModifier BONUS_ATTACK_MODIFIER = new BuffUtil.SimpleAdditionBuffModifier(Components.Stats.BONUS_ATTACK);
    private static final BuffUtil.BuffModifier BONUS_HEALTH_MODIFIER = new BuffUtil.SimpleAdditionBuffModifier(Components.Stats.BONUS_HEALTH);

    public static Integer getEffectiveAttack(EntityData data, int entity) {
        Integer attack = data.getComponent(entity, Components.Stats.ATTACK);
        if (attack != null) {
            attack += getBonusAttack(data, entity);
        }
        return attack;
    }

    public static int getBonusAttack(EntityData data, int entity) {
        return BuffUtil.modifyViaBuffs(data, entity, 0, BONUS_ATTACK_MODIFIER);
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
        return BuffUtil.modifyViaBuffs(data, entity, 0, BONUS_HEALTH_MODIFIER);
    }

    public static boolean isDamaged(EntityData data, int entity) {
        return data.hasComponent(entity, Components.Stats.DAMAGED) || data.hasComponent(entity, Components.Stats.BONUS_DAMAGED);
    }
}
