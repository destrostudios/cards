package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class SpellUtil {

    public static boolean isTargeted(EntityData data, int entity) {
        return data.hasComponent(entity, Components.Target.TARGET_PREFILTER);
    }

    public static boolean isCastable_WithoutSpellCondition(EntityData data, int card, int spell) {
        Integer maximumCastsPerTurn = data.getComponent(spell, Components.Spell.MAXIMUM_CASTS_PER_TURN);
        if (maximumCastsPerTurn != null) {
            int currentCastsPerTurn = data.getOptionalComponent(spell, Components.Spell.CURRENT_CASTS_PER_TURN).orElse(0);
            if (currentCastsPerTurn >= maximumCastsPerTurn) {
                return false;
            }
        }
        int player = data.getComponent(card, Components.OWNED_BY);
        return CostUtil.isPayable(data, player, spell);
    }

    public static boolean isCastable_OnlySpellCondition(EntityData data, int card, int spell, int[] targets) {
        return ConditionUtil.isConditionFulfilled(data, spell, card, targets);
    }

    // TODO: Maybe just mark the default spells (cast from hand + attack) as such, saves this whole logic and also performance, especially on conditions

    public static boolean isDefaultCastFromHandSpell(EntityData data, int spell) {
        String conditionExpression = data.getComponent(spell, Components.CONDITION);
        if (conditionExpression != null) {
            // Currently, all spells with a source.isInHand condition are defaultCastFromHandSpells
            return conditionExpression.contains("source.isInHand");
        }
        return false;
    }

    public static boolean isDefaultAttackSpell(EntityData data, int spell) {
        int[] instantEffectTriggers = data.getComponent(spell, Components.Spell.INSTANT_EFFECT_TRIGGERS);
        if (instantEffectTriggers != null) {
            for (int instantEffectTrigger : instantEffectTriggers) {
                int[] effects = data.getComponent(instantEffectTrigger, Components.EffectTrigger.EFFECTS);
                for (int effect : effects) {
                    if (data.hasComponent(effect, Components.Effect.BATTLE)) {
                        int[] targetDefinitions = data.getComponent(effect, Components.Target.TARGETS);
                        for (int targetDefinition : targetDefinitions) {
                            String targetExpression = data.getComponent(targetDefinition, Components.Target.TARGET);
                            if (targetExpression.equals("targets")) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static Integer getCaster(EntityData data, int spell) {
        // TODO: Performance would of course be better with a direct link
        for (int caster : data.query(Components.SPELLS).list()) {
            if (ArrayUtil.contains(data, caster, Components.SPELLS, spell)) {
                return caster;
            }
        }
        return null;
    }
}
