package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

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
