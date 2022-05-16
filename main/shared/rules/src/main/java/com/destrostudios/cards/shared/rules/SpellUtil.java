package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;

public class SpellUtil {

    public static boolean isTargeted(EntityData data, int entity) {
        int[] conditions = data.getComponent(entity, Components.CONDITIONS);
        if (conditions != null) {
            for (int condition : conditions) {
                if (data.hasComponent(condition, Components.Target.TARGET_TARGET)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCastable(EntityData data, int card, int spell, int[] targets) {
        if (!ConditionUtil.areConditionsFulfilled(data, spell, card, targets)) {
            return false;
        }
        Integer cost = data.getComponent(spell, Components.COST);
        if (cost != null) {
            int player = data.getComponent(card, Components.OWNED_BY);
            if (!CostUtil.isPayable(data, player, cost)) {
                return false;
            }
        }
        return true;
    }
}
