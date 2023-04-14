package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class CostUtil {

    private static final BuffUtil.BuffModifier MANA_COST_BUFF_MODIFIER = new BuffUtil.SimpleBuffModifier(Components.Cost.BONUS_MANA_COST, Components.Cost.SET_MANA_COST);

    public static boolean isPayable(EntityData data, int player, int spell) {
        Integer manaCost = getEffectiveManaCost(data, spell);
        if (manaCost != null) {
            return manaCost <= data.getComponent(player, Components.MANA);
        }
        return true;
    }

    public static Integer getEffectiveManaCost(EntityData data, int spell) {
        BuffUtil.BuffCalculationResult result = calculateManaCost(data, spell);
        if (result != null) {
            return Math.max(0, result.getEffectiveValue());
        }
        return null;
    }

    private static BuffUtil.BuffCalculationResult calculateManaCost(EntityData data, int spell) {
        return BuffUtil.calculateWithBuffs(data, spell, Components.Cost.MANA_COST, MANA_COST_BUFF_MODIFIER);
    }
}
