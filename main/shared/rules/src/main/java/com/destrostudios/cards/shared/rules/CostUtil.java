package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;

public class CostUtil {

    public static boolean isPayable(EntityData data, int player, int cost) {
        Integer manaCost = data.getComponent(cost, Components.MANA);
        if (manaCost != null) {
            int availableMana = data.getOptionalComponent(player, Components.MANA).orElse(0);
            if (manaCost > availableMana) {
                return false;
            }
        }
        return true;
    }
}
