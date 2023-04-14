package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.Prefilter;

public class SpellUtil {

    public static boolean isTargeted(EntityData data, int entity) {
        return data.hasComponent(entity, Components.Target.TARGET_PREFILTERS);
    }

    public static boolean isCastable_WithoutSpellCondition(EntityData data, int card, int spell) {
        Integer maximumCastsPerTurn = data.getComponent(spell, Components.Spell.MAXIMUM_CASTS_PER_TURN);
        if (maximumCastsPerTurn != null) {
            Integer currentCastsPerTurn = data.getComponent(spell, Components.Spell.CURRENT_CASTS_PER_TURN);
            if ((currentCastsPerTurn != null) && (currentCastsPerTurn >= maximumCastsPerTurn)) {
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

    public static Integer getDefaultCastFromHandSpell(EntityData data, int card) {
        int[] spells = data.getComponent(card, Components.SPELLS);
        for (int spell : spells) {
            if (isDefaultCastFromHandSpell(data, spell)) {
                return spell;
            }
        }
        return null;
    }

    public static boolean isDefaultCastFromHandSpell(EntityData data, int spell) {
        // Currently, all spells with a hand prefilter are defaultCastFromHandSpells
        Prefilter[] prefilters = data.getComponent(spell, Components.Target.SOURCE_PREFILTERS);
        return ((prefilters != null) && (prefilters[0] == Prefilter.HAND));
    }

    public static boolean isDefaultAttackSpell(EntityData data, int spell) {
        int[] castTriggers = data.getComponent(spell, Components.Spell.CAST_TRIGGERS);
        if (castTriggers != null) {
            for (int castTrigger : castTriggers) {
                int[] effects = data.getComponent(castTrigger, Components.Trigger.EFFECTS);
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
}
