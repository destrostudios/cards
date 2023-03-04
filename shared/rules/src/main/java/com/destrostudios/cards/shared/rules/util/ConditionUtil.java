package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ConditionUtil {

    private static final List<ComponentDefinition<?>> simpleRequireableComponents = new LinkedList<>();
    static {
        simpleRequireableComponents.add(Components.NAME);
        simpleRequireableComponents.add(Components.CREATURE_CARD);
        simpleRequireableComponents.add(Components.SPELL_CARD);
        simpleRequireableComponents.add(Components.Ability.SLOW);
        simpleRequireableComponents.add(Components.Ability.DIVINE_SHIELD);
        simpleRequireableComponents.add(Components.Ability.HEXPROOF);
        simpleRequireableComponents.add(Components.Ability.IMMUNE);
        simpleRequireableComponents.add(Components.Ability.TAUNT);
        simpleRequireableComponents.add(Components.Tribe.BEAST);
        simpleRequireableComponents.add(Components.Tribe.DRAGON);
        simpleRequireableComponents.add(Components.Tribe.FISH);
        simpleRequireableComponents.add(Components.Tribe.GOD);
        simpleRequireableComponents.add(Components.Tribe.HUMAN);
    }

    public static boolean areConditionsFulfilled(EntityData data, int entity, int source, int[] targets) {
        int[] conditions = data.getComponent(entity, Components.CONDITIONS);
        if (conditions != null) {
            return areConditionsFulfilled(data, conditions, source, targets);
        }
        return true;
    }

    public static boolean areConditionsFulfilled(EntityData data, int[] conditions, int source, int[] targets) {
        for (int condition : conditions) {
            if (!isFulfilled(data, condition, source, targets)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isOneConditionFulfilled(EntityData data, int[] conditions, int source, int[] targets) {
        for (int condition : conditions) {
            if (isFulfilled(data, condition, source, targets)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFulfilled(EntityData data, int condition, int source, int[] targets) {
        int[] oneOfConditions = data.getComponent(condition, Components.Condition.ONE_OF);
        if (oneOfConditions != null) {
            return isOneConditionFulfilled(data, oneOfConditions, source, targets);
        } else {
            int[] targetChains = data.getComponent(condition, Components.Target.TARGET_CHAINS);
            List<Integer> affectedTargets = TargetUtil.getAffectedTargets(data, targetChains, source, targets);
            return isFulfilled(data, condition, source, affectedTargets);
        }
    }

    private static boolean isFulfilled(EntityData data, int condition, int source, List<Integer> targets) {
        boolean isNegated = data.hasComponent(condition, Components.Condition.NOT);
        for (int target : targets) {
            if (isFulfilledIgnoringNegation(data, condition, source, target) == isNegated) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFulfilledIgnoringNegation(EntityData data, int condition, int source, int target) {
        if (data.hasComponent(condition, Components.Condition.PLAYER)) {
            if (!data.hasComponent(target, Components.NEXT_PLAYER)) {
                return false;
            }
        }
        if (data.hasComponent(condition, Components.Condition.ALLY)) {
            if (checkAllyOrOpponent(data, source, target, false)) {
                return false;
            }
        }
        if (data.hasComponent(condition, Components.Condition.OPPONENT)) {
            if (checkAllyOrOpponent(data, source, target, true)) {
                return false;
            }
        }
        if (data.hasComponent(condition, Components.Condition.IN_LIBRARY) && !data.hasComponent(target, Components.LIBRARY)) {
            return false;
        }
        if (data.hasComponent(condition, Components.Condition.IN_HAND) && !data.hasComponent(target, Components.HAND)) {
            return false;
        }
        if (data.hasComponent(condition, Components.Condition.ON_BOARD) && !data.hasComponent(target, Components.BOARD)) {
            return false;
        }
        if (data.hasComponent(condition, Components.Condition.IN_GRAVEYARD) && !data.hasComponent(target, Components.GRAVEYARD)) {
            return false;
        }
        Integer minimumManaCost = data.getComponent(condition, Components.Condition.MINIMUM_MANA_COST);
        Integer maximumManaCost = data.getComponent(condition, Components.Condition.MAXIMUM_MANA_COST);
        if ((minimumManaCost != null) || (maximumManaCost != null)) {
            int[] spells = data.getComponent(target, Components.SPELLS);
            if (spells != null) {
                for (int spell : spells) {
                    if (SpellUtil.isDefaultCastFromHandSpell(data, spell)) {
                        Integer manaCost = CostUtil.getEffectiveManaCost(data, spell);
                        if ((minimumManaCost != null) && (manaCost < minimumManaCost)) {
                            return false;
                        }
                        if ((maximumManaCost != null) && (manaCost > maximumManaCost)) {
                            return false;
                        }
                        break;
                    }
                }
            }
        }
        if (data.hasComponent(condition, Components.Condition.NO_CREATURES)) {
            int creatures = data.query(Components.CREATURE_ZONE).list(card -> data.getComponent(card, Components.OWNED_BY) == target).size();
            if (creatures > 0) {
                return false;
            }
        }
        if (data.hasComponent(condition, Components.Condition.DEFAULT_CAST_FROM_HAND_SPELL) && !SpellUtil.isDefaultCastFromHandSpell(data, target)) {
            return false;
        }
        for (ComponentDefinition<?> componentDefinition : simpleRequireableComponents) {
            // Check has instead of (get != null) because of Void type
            if (data.hasComponent(condition, componentDefinition)) {
                Object requiredComponent = data.getComponent(condition, componentDefinition);
                if (data.hasComponent(target, componentDefinition)) {
                    Object targetComponent = data.getComponent(target, componentDefinition);
                    if (!Objects.equals(requiredComponent, targetComponent)) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkAllyOrOpponent(EntityData entityData, int source, int target, boolean expectsAllied) {
        Integer sourceOwner = entityData.getComponent(source, Components.OWNED_BY);
        Integer targetOwner = entityData.getComponent(target, Components.OWNED_BY);
        boolean isAllied = (Objects.equals(sourceOwner, targetOwner) || Objects.equals(sourceOwner, target));
        return (isAllied == expectsAllied);
    }
}
