package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class CostUtil {

    private static final BuffUtil.BuffModifier MANA_COST_BUFF_MODIFIER = new BuffUtil.SimpleBuffModifier(Components.Cost.BONUS_MANA_COST, Components.Cost.SET_MANA_COST);

    public static boolean isPayable(EntityData data, int player, int entity) {
        Integer manaCost = getEffectiveManaCost(data, entity);
        if (manaCost != null) {
            int availableMana = data.getOptionalComponent(player, Components.MANA).orElse(0);
            return manaCost <= availableMana;
        }
        return true;
    }

    public static Integer getEffectiveManaCost(EntityData data, int entity) {
        BuffUtil.BuffCalculationResult result = calculateManaCost(data, entity);
        if (result != null) {
            return Math.max(0, result.getEffectiveValue());
        }
        return null;
    }

    private static BuffUtil.BuffCalculationResult calculateManaCost(EntityData data, int entity) {
        return BuffUtil.calculateWithBuffs(data, entity, Components.Cost.MANA_COST, MANA_COST_BUFF_MODIFIER);
    }
}
