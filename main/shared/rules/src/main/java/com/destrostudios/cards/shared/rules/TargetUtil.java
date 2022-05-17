package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;

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
            for (int target : data.query(Components.OWNED_BY).list()) {
                if (ConditionUtil.areConditionsFulfilled(data, conditions, source, new int[] { target })) {
                    affectedTargets.add(target);
                }
            }
        }
        return affectedTargets;
    }
}
