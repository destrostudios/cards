package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ConditionUtil {

    private static final List<ComponentDefinition<?>> simpleRequirableComponents = new LinkedList<>();
    static {
        simpleRequirableComponents.add(Components.ATTACK);
        simpleRequirableComponents.add(Components.CREATURE_CARD);
        simpleRequirableComponents.add(Components.NAME);
        simpleRequirableComponents.add(Components.HEALTH);
        simpleRequirableComponents.add(Components.DAMAGED);
        simpleRequirableComponents.add(Components.SPELL_CARD);
        simpleRequirableComponents.add(Components.Ability.SLOW);
        simpleRequirableComponents.add(Components.Ability.DIVINE_SHIELD);
        simpleRequirableComponents.add(Components.Ability.HEXPROOF);
        simpleRequirableComponents.add(Components.Ability.IMMUNE);
        simpleRequirableComponents.add(Components.Ability.TAUNT);
        simpleRequirableComponents.add(Components.Tribe.BEAST);
        simpleRequirableComponents.add(Components.Tribe.DRAGON);
        simpleRequirableComponents.add(Components.Tribe.FISH);
        simpleRequirableComponents.add(Components.Tribe.GOD);
        simpleRequirableComponents.add(Components.Tribe.HUMAN);
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
            return isFulfilled(data, condition, source, TargetUtil.getAffectedTargets(data, condition, source, targets));
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
        if (data.hasComponent(condition, Components.Condition.IN_HAND) && !data.hasComponent(target, Components.HAND)) {
            return false;
        }
        if (data.hasComponent(condition, Components.Condition.ON_BOARD) && !data.hasComponent(target, Components.BOARD)) {
            return false;
        }
        if (data.hasComponent(condition, Components.Condition.NO_CREATURES)) {
            int creatures = data.query(Components.CREATURE_ZONE).list(card -> data.getComponent(card, Components.OWNED_BY) == target).size();
            if (creatures > 0) {
                return false;
            }
        }
        for (ComponentDefinition<?> componentDefinition : simpleRequirableComponents) {
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

    public static boolean isTargetConditionIncluded(EntityData data, int[] conditions) {
        for (int condition : conditions) {
            int[] oneOfConditions = data.getComponent(condition, Components.Condition.ONE_OF);
            if (oneOfConditions != null) {
                if (isTargetConditionIncluded(data, oneOfConditions)) {
                    return true;
                }
            } else if (data.hasComponent(condition, Components.Target.TARGET_TARGETS)) {
                return true;
            }
        }
        return false;
    }
}
