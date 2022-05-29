package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class SpellUtil {

    public static boolean isTargeted(EntityData data, int entity) {
        int[] conditions = data.getComponent(entity, Components.CONDITIONS);
        if (conditions != null) {
            for (int condition : conditions) {
                if (data.hasComponent(condition, Components.Target.TARGET_TARGETS)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCastable(EntityData data, int card, int spell, int[] targets) {
        Integer maximumCastsPerTurn = data.getComponent(spell, Components.Spell.MAXIMUM_CASTS_PER_TURN);
        if (maximumCastsPerTurn != null) {
            int currentCastsPerTurn = data.getOptionalComponent(spell, Components.Spell.CURRENT_CASTS_PER_TURN).orElse(0);
            if (currentCastsPerTurn >= maximumCastsPerTurn) {
                return false;
            }
        }
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
