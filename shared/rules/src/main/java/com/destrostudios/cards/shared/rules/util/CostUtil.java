package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class CostUtil {

    private static final BuffUtil.BuffModifier BONUS_MANA_COST_MODIFIER = new BuffUtil.SimpleAdditionBuffModifier(Components.Cost.BONUS_MANA_COST);

    public static boolean isPayable(EntityData data, int player, int entity) {
        Integer manaCost = getEffectiveManaCost(data, entity);
        if (manaCost != null) {
            int availableMana = data.getOptionalComponent(player, Components.MANA).orElse(0);
            return manaCost <= availableMana;
        }
        return true;
    }

    public static Integer getEffectiveManaCost(EntityData data, int entity) {
        Integer manaCost = data.getComponent(entity, Components.Cost.MANA_COST);
        if (manaCost != null) {
            manaCost += getBonusManaCost(data, entity);
        }
        return manaCost;
    }

    public static int getBonusManaCost(EntityData data, int entity) {
        return BuffUtil.modifyViaBuffs(data, entity, 0, BONUS_MANA_COST_MODIFIER);
    }
}
