package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.LinkedList;
import java.util.List;

public class TargetUtil {

    public static List<Integer> getAffectedTargets(EntityData data, int targetRuleEntity, int source, int[] targets) {
        LinkedList<Integer> affectedTargets = new LinkedList<>();
        if (data.hasComponent(targetRuleEntity, Components.Target.SOURCE_TARGET)) {
            affectedTargets.add(source);
        }
        if (data.hasComponent(targetRuleEntity, Components.Target.TARGET_TARGETS)) {
            for (int target : targets) {
                affectedTargets.add(target);
            }
        }
        int[] conditions = data.getComponent(targetRuleEntity, Components.Target.CONDITION_TARGETS);
        if (conditions != null) {
            // TODO: Unify
            addAffectedTargets(data, conditions, source, data.query(Components.OWNED_BY).list(), affectedTargets);
            addAffectedTargets(data, conditions, source, data.query(Components.NEXT_PLAYER).list(), affectedTargets);
        }
        return affectedTargets;
    }

    private static void addAffectedTargets(EntityData data, int[] conditions, int source, List<Integer> targetsToCheck, LinkedList<Integer> affectedTargets) {
        for (int target : targetsToCheck) {
            if (ConditionUtil.areConditionsFulfilled(data, conditions, source, new int[] { target })) {
                affectedTargets.add(target);
            }
        }
    }
}
