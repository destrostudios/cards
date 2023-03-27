package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.expressions.Expressions;

import java.util.Objects;

public class ConditionUtil {

    public static boolean isConditionFulfilled(EntityData data, int entity, int source, int[] targets) {
        String condition = data.getComponent(entity, Components.CONDITION);
        if (condition != null) {
            return isConditionFulfilled(data, condition, source, targets);
        }
        return true;
    }

    public static boolean isConditionFulfilled(EntityData data, String condition, int source, int[] targets) {
        if (targets.length > 0) {
            for (int target : targets) {
                if (!evaluateCondition(data, condition, source, target)) {
                    return false;
                }
            }
            return true;
        }
        return evaluateCondition(data, condition, source, null);
    }

    private static boolean evaluateCondition(EntityData data, String condition, int source, Integer target) {
        return Expressions.evaluate(data, condition, source, target);
    }

    public static boolean isAlly(EntityData data, int entity1, int entity2) {
        if (entity1 == entity2) {
            return true;
        }
        Integer owner1 = data.getComponent(entity1, Components.OWNED_BY);
        Integer owner2 = data.getComponent(entity2, Components.OWNED_BY);
        return (Objects.equals(owner1, owner2) || Objects.equals(entity1, owner2) || Objects.equals(entity2, owner1));
    }
}
