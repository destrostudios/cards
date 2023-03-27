package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TargetPrefilter;
import com.destrostudios.cards.shared.rules.expressions.Expressions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TargetUtil {

    public static List<Integer> getAffectedTargets(EntityData data, int[] targetDefinitions, int source, int[] targets) {
        // TODO: Use Set so entities are not affected multiple times? Dependent on effect/context? (here and in the methods below)
        LinkedList<Integer> affectedTargets = new LinkedList<>();
        for (int targetDefinition : targetDefinitions) {
            affectedTargets.addAll(getAffectedTargets(data, targetDefinition, source, targets));
        }
        return affectedTargets;
    }

    private static LinkedList<Integer> getAffectedTargets(EntityData data, int targetDefinition, int source, int[] targets) {
        LinkedList<Integer> affectedTargets = new LinkedList<>();
        String targetExpression = data.getComponent(targetDefinition, Components.Target.TARGET);
        if (targetExpression != null) {
            int[] evaluatedTargets = Expressions.evaluateEntities(data, targetExpression, source, targets);
            for (int target : evaluatedTargets) {
                affectedTargets.add(target);
            }
        }
        String targetAllCondition = data.getComponent(targetDefinition, Components.Target.TARGET_ALL);
        if (targetAllCondition != null) {
            List<Integer> prefilteredTargets = getPrefilteredTargets(data, targetDefinition);
            affectedTargets.addAll(getAllConditionTargets(data, prefilteredTargets, targetAllCondition, source));
        }
        Integer maxRandomTargets = data.getComponent(targetDefinition, Components.Target.TARGET_RANDOM);
        if (maxRandomTargets != null) {
            // TODO: Use random game-tools interface?
            Collections.shuffle(affectedTargets);
            while (affectedTargets.size() > maxRandomTargets) {
                affectedTargets.removeLast();
            }
        }
        return affectedTargets;
    }

    public static List<Integer> getPrefilteredTargets(EntityData data, int entity) {
        TargetPrefilter targetPrefilter = data.getComponent(entity, Components.Target.TARGET_PREFILTER);
        ComponentDefinition<?> prefilterComponent = null;
        switch (targetPrefilter) {
            case LIBRARY -> prefilterComponent = Components.LIBRARY;
            case BOARD -> prefilterComponent = Components.BOARD;
            case HAND -> prefilterComponent = Components.HAND;
            case GRAVEYARD -> prefilterComponent = Components.GRAVEYARD;
        }
        return data.query(prefilterComponent).list();
    }

    private static List<Integer> getAllConditionTargets(EntityData data, List<Integer> prefilteredTargets, String condition, int source) {
        if (condition.isEmpty()) {
            return prefilteredTargets;
        }
        return prefilteredTargets.stream()
                .filter(target -> ConditionUtil.isConditionFulfilled(data, condition, source, new int[] { target }))
                .collect(Collectors.toList());
    }
}
