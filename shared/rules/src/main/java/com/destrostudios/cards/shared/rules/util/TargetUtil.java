package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TargetPrefilter;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TargetUtil {

    public static List<Integer> getAffectedTargets(EntityData data, int[] targetDefinitions, int source, int[] targets, NetworkRandom random) {
        // TODO: Use Set so entities are not affected multiple times? Dependent on effect/context? (here and in the methods below)
        LinkedList<Integer> affectedTargets = new LinkedList<>();
        for (int targetDefinition : targetDefinitions) {
            affectedTargets.addAll(getAffectedTargets(data, targetDefinition, source, targets, random));
        }
        return affectedTargets;
    }

    private static LinkedList<Integer> getAffectedTargets(EntityData data, int targetDefinition, int source, int[] targets, NetworkRandom random) {
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
            TargetPrefilter targetPrefilter = data.getComponent(targetDefinition, Components.Target.TARGET_PREFILTER);
            affectedTargets.addAll(getAllConditionTargets(data, source, targetPrefilter, targetAllCondition));
        }
        String maxRandomTargetsExpression = data.getComponent(targetDefinition, Components.Target.TARGET_RANDOM);
        if (maxRandomTargetsExpression != null) {
            int maxRandomTargets = Expressions.evaluate(data, maxRandomTargetsExpression, source, targets);
            while (affectedTargets.size() > maxRandomTargets) {
                affectedTargets.remove(random.nextInt(affectedTargets.size() - 1));
            }
        }
        return affectedTargets;
    }

    public static List<Integer> getAllConditionTargets(EntityData data, int source, TargetPrefilter targetPrefilter, String condition) {
        List<Integer> prefilteredTargets = getPrefilteredTargets(data, targetPrefilter);
        if (condition.isEmpty()) {
            return prefilteredTargets;
        }
        return prefilteredTargets.stream()
                .filter(target -> ConditionUtil.isConditionFulfilled(data, condition, source, new int[] { target }))
                .collect(Collectors.toList());
    }

    public static List<Integer> getPrefilteredTargets(EntityData data, TargetPrefilter targetPrefilter) {
        ComponentDefinition<?> prefilterComponent = null;
        switch (targetPrefilter) {
            case BOARD -> prefilterComponent = Components.BOARD;
            case CREATURE_ZONE -> prefilterComponent = Components.CREATURE_ZONE;
            case GRAVEYARD -> prefilterComponent = Components.GRAVEYARD;
            case HAND -> prefilterComponent = Components.HAND;
            case LIBRARY -> prefilterComponent = Components.LIBRARY;
        }
        return data.query(prefilterComponent).list();
    }
}
