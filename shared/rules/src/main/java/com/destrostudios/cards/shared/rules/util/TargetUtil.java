package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.Prefilter;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TargetUtil {

    public static List<Integer> getAffectedTargets(EntityData data, int[] targetDefinitions, int source, Event event, NetworkRandom random) {
        // TODO: Use Set so entities are not affected multiple times? Dependent on effect/context? (here and in the methods below)
        LinkedList<Integer> affectedTargets = new LinkedList<>();
        for (int targetDefinition : targetDefinitions) {
            affectedTargets.addAll(getAffectedTargets(data, targetDefinition, source, event, random));
        }
        return affectedTargets;
    }

    private static LinkedList<Integer> getAffectedTargets(EntityData data, int targetDefinition, int source, Event event, NetworkRandom random) {
        LinkedList<Integer> affectedTargets = new LinkedList<>();
        if (isFulfillingPrefilter_Source(data, targetDefinition, source)) {
            String targetExpression = data.getComponent(targetDefinition, Components.Target.TARGET);
            if (targetExpression != null) {
                int[] evaluatedTargets = Expressions.evaluateEntities(targetExpression, Expressions.getContext_Event(data, event));
                for (int target : evaluatedTargets) {
                    affectedTargets.add(target);
                }
            }
            String targetAllCondition = data.getComponent(targetDefinition, Components.Target.TARGET_ALL);
            if (targetAllCondition != null) {
                Prefilter[] targetPrefilters = data.getComponent(targetDefinition, Components.Target.TARGET_PREFILTERS);
                affectedTargets.addAll(getAllConditionTargets(data, source, targetPrefilters, targetAllCondition));
            }
            String maxRandomTargetsExpression = data.getComponent(targetDefinition, Components.Target.TARGET_RANDOM);
            if (maxRandomTargetsExpression != null) {
                int maxRandomTargets = Expressions.evaluate(maxRandomTargetsExpression, Expressions.getContext_Event(data, event));
                while (affectedTargets.size() > maxRandomTargets) {
                    affectedTargets.remove(random.nextInt(affectedTargets.size() - 1));
                }
            }
        }
        return affectedTargets;
    }

    public static List<Integer> getAllConditionTargets(EntityData data, int source, Prefilter[] targetPrefilters, String condition) {
        List<Integer> prefilteredTargets = getPrefilteredEntities(data, source, targetPrefilters);
        if (condition.isEmpty()) {
            return prefilteredTargets;
        }
        return prefilteredTargets.stream()
                .filter(target -> ConditionUtil.isConditionFulfilled(data, source, new int[] { target }, null, condition))
                .collect(Collectors.toList());
    }

    public static List<Integer> getPrefilteredEntities(EntityData data, int source, Prefilter[] prefilters) {
        return data.query(getBasicPrefilterComponent(prefilters[0])).list(entity -> isFulfillingPrefilters(data, entity, source, prefilters));
    }

    public static boolean isFulfillingPrefilter_Source(EntityData data, int entity, int source) {
        return isFulfillingPrefilter(data, source, source, entity, Components.Target.SOURCE_PREFILTERS);
    }

    private static boolean isFulfillingPrefilter(EntityData data, int entity, int source, int entityWithPrefilters, ComponentDefinition<Prefilter[]> prefiltersComponent) {
        Prefilter[] prefilters = data.getComponent(entityWithPrefilters, prefiltersComponent);
        return ((prefilters == null) || isFulfillingPrefilters(data, entity, source, prefilters));
    }

    public static boolean isFulfillingPrefilters(EntityData data, int entity, int source, Prefilter[] prefilters) {
        for (Prefilter prefilter : prefilters) {
            if (!isFulfillingPrefilter(data, entity, source, prefilter)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFulfillingPrefilter(EntityData data, int entity, int source, Prefilter prefilter) {
        switch (prefilter) {
            case SOURCE -> { return entity == source; }
            default -> { return data.hasComponent(entity, getBasicPrefilterComponent(prefilter)); }
        }
    }

    private static ComponentDefinition<?> getBasicPrefilterComponent(Prefilter prefilter) {
        switch (prefilter) {
            case BOARD -> { return Components.BOARD; }
            case CREATURE_ZONE -> { return Components.CREATURE_ZONE; }
            case GRAVEYARD -> { return Components.GRAVEYARD; }
            case HAND -> { return Components.HAND; }
            case LIBRARY -> { return Components.LIBRARY; }
        }
        throw new IllegalArgumentException();
    }
}
