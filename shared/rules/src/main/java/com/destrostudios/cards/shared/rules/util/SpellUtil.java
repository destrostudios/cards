package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class SpellUtil {

    public static boolean isTargeted(EntityData data, int entity) {
        return data.hasComponent(entity, Components.Target.TARGET_PREFILTER);
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

    public static boolean isDefaultCastFromHandSpell(EntityData data, int spell) {
        int[] conditions = data.getComponent(spell, Components.CONDITIONS);
        if (conditions != null) {
            for (int condition : conditions) {
                if (!data.hasComponent(condition, Components.Condition.IN_HAND)) {
                    int[] targetChains = data.getComponent(condition, Components.Target.TARGET_CHAINS);
                    if (targetChains != null) {
                        for (int targetChain : targetChains) {
                            int[] targetChainSteps = data.getComponent(targetChain, Components.Target.TARGET_CHAIN);
                            int initialTargetChainStep = targetChainSteps[0];
                            if (data.hasComponent(initialTargetChainStep, Components.Target.TARGET_SOURCE)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean isDefaultAttackSpell(EntityData data, int spell) {
        int[] instantEffectTriggers = data.getComponent(spell, Components.Spell.INSTANT_EFFECT_TRIGGERS);
        if (instantEffectTriggers != null) {
            for (int instantEffectTrigger : instantEffectTriggers) {
                int[] effects = data.getComponent(instantEffectTrigger, Components.EffectTrigger.EFFECTS);
                for (int effect : effects) {
                    if (data.hasComponent(effect, Components.Effect.BATTLE)) {
                        int[] targetChains = data.getComponent(effect, Components.Target.TARGET_CHAINS);
                        for (int targetChain : targetChains) {
                            int[] targetChainSteps = data.getComponent(targetChain, Components.Target.TARGET_CHAIN);
                            int initialTargetChainStep = targetChainSteps[0];
                            if (data.hasComponent(initialTargetChainStep, Components.Target.TARGET_TARGETS)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
