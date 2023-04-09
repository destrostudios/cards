package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.ZonePrefilter;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.apache.commons.jexl3.JexlContext;

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
                ZonePrefilter targetPrefilter = data.getComponent(targetDefinition, Components.Target.TARGET_PREFILTER);
                affectedTargets.addAll(getAllConditionTargets(data, source, targetPrefilter, targetAllCondition));
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

    public static List<Integer> getAllConditionTargets(EntityData data, int source, ZonePrefilter targetPrefilter, String condition) {
        List<Integer> prefilteredTargets = getPrefilteredEntities(data, targetPrefilter);
        if (condition.isEmpty()) {
            return prefilteredTargets;
        }
        return prefilteredTargets.stream()
                .filter(target -> ConditionUtil.isConditionFulfilled(data, source, new int[] { target }, targetPrefilter, condition))
                .collect(Collectors.toList());
    }

    public static List<Integer> getPrefilteredEntities(EntityData data, ZonePrefilter zonePrefilter) {
        return data.query(getPrefilterComponent(zonePrefilter)).list();
    }

    public static boolean isFulfillingPrefilter_Source(EntityData data, int entity, int source) {
        return isFulfillingPrefilter(data, entity, Components.Target.SOURCE_PREFILTER, source);
    }

    private static boolean isFulfillingPrefilter(EntityData data, int entity, ComponentDefinition<ZonePrefilter> prefilterComponent, int prefilteredEntity) {
        ZonePrefilter zonePrefilter = data.getComponent(entity, prefilterComponent);
        return ((zonePrefilter == null) || isFulfillingPrefilter(data, prefilteredEntity, zonePrefilter));
    }

    public static boolean isFulfillingPrefilter(EntityData data, int entity, ZonePrefilter zonePrefilter) {
        return data.hasComponent(entity, getPrefilterComponent(zonePrefilter));
    }

    private static ComponentDefinition<?> getPrefilterComponent(ZonePrefilter zonePrefilter) {
        switch (zonePrefilter) {
            case BOARD -> { return Components.BOARD; }
            case CREATURE_ZONE -> { return Components.CREATURE_ZONE; }
            case GRAVEYARD -> { return Components.GRAVEYARD; }
            case HAND -> { return Components.HAND; }
            case LIBRARY -> { return Components.LIBRARY; }
        }
        throw new IllegalArgumentException();
    }
}
