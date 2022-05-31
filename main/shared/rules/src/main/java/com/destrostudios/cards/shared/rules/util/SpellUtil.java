package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class SpellUtil {

    public static boolean isTargeted(EntityData data, int entity) {
        int[] conditions = data.getComponent(entity, Components.CONDITIONS);
        if (conditions != null) {
            return ConditionUtil.isTargetConditionIncluded(data, conditions);
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

    public static boolean isDefaultCastFromHandSpell(EntityData entityData, int spell) {
        int[] conditions = entityData.getComponent(spell, Components.CONDITIONS);
        if (conditions != null) {
            for (int condition : conditions) {
                if (entityData.hasComponent(condition, Components.Target.SOURCE_TARGET) && !entityData.hasComponent(condition, Components.Condition.IN_HAND)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isDefaultAttackSpell(EntityData entityData, int spell) {
        int[] instantEffectTriggers = entityData.getComponent(spell, Components.Spell.INSTANT_EFFECT_TRIGGERS);
        if (instantEffectTriggers != null) {
            for (int instantEffectTrigger : instantEffectTriggers) {
                int[] effects = entityData.getComponent(instantEffectTrigger, Components.EffectTrigger.EFFECTS);
                for (int effect : effects) {
                    if (entityData.hasComponent(effect, Components.Target.TARGET_TARGETS) && entityData.hasComponent(effect, Components.Effect.BATTLE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
