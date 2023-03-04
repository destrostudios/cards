package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TargetPrefilter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TargetUtil {

    public static List<Integer> getAffectedTargets(EntityData data, int[] targetChains, int source, int[] targets) {
        // TODO: Use Set so entities are not affected multiple times? Dependent on effect/context?
        LinkedList<Integer> affectedTargets = new LinkedList<>();
        for (int targetChain : targetChains) {
            affectedTargets.addAll(getAffectedTargets(data, targetChain, source, targets));
        }
        return affectedTargets;
    }

    private static LinkedList<Integer> getAffectedTargets(EntityData data, int targetChain, int source, int[] targets) {
        int[] targetChainSteps = data.getComponent(targetChain, Components.Target.TARGET_CHAIN);
        LinkedList<Integer> currentStepTargets = new LinkedList<>();
        for (int i = 0; i < targetChainSteps.length; i++) {
            if (i == 0) {
                currentStepTargets = getInitialStepTargets(data, targetChainSteps[i], source, targets);
            } else {
                currentStepTargets = transformCurrentStepTargets(data, targetChainSteps[i], currentStepTargets);
            }
        }
        return currentStepTargets;
    }

    private static LinkedList<Integer> getInitialStepTargets(EntityData data, int targetChainStep, int source, int[] targets) {
        LinkedList<Integer> initialTargets = new LinkedList<>();
        if (data.hasComponent(targetChainStep, Components.Target.TARGET_SOURCE)) {
            initialTargets.add(source);
        }
        if (data.hasComponent(targetChainStep, Components.Target.TARGET_TARGETS)) {
            for (int target : targets) {
                initialTargets.add(target);
            }
        }
        int[] customTargets = data.getComponent(targetChainStep, Components.Target.TARGET_CUSTOM);
        if (customTargets != null) {
            for (int customTarget : customTargets) {
                initialTargets.add(customTarget);
            }
        }
        int[] conditions = data.getComponent(targetChainStep, Components.Target.TARGET_ALL);
        if (conditions != null) {
            List<Integer> prefilteredTargets = getPrefilteredTargets(data, targetChainStep);
            initialTargets.addAll(getAllConditionTargets(data, prefilteredTargets, conditions, source));
        }
        return initialTargets;
    }

    private static LinkedList<Integer> getAllConditionTargets(EntityData data, List<Integer> prefilteredTarget, int[] conditions, int source) {
        LinkedList<Integer> targets = new LinkedList<>();
        for (int target : prefilteredTarget) {
            if (ConditionUtil.areConditionsFulfilled(data, conditions, source, new int[] { target })) {
                targets.add(target);
            }
        }
        return targets;
    }

    public static List<Integer> getPrefilteredTargets(EntityData data, int entity) {
        TargetPrefilter targetPrefilter = data.getComponent(entity, Components.Target.TARGET_PREFILTER);
        ComponentDefinition<?> prefilterComponent = null;
        switch (targetPrefilter) {
            case PLAYER -> prefilterComponent = Components.NEXT_PLAYER;
            case LIBRARY -> prefilterComponent = Components.LIBRARY;
            case BOARD -> prefilterComponent = Components.BOARD;
            case HAND -> prefilterComponent = Components.HAND;
            case GRAVEYARD -> prefilterComponent = Components.GRAVEYARD;
        }
        return data.query(prefilterComponent).list();
    }

    private static LinkedList<Integer> transformCurrentStepTargets(EntityData data, int targetChainStep, LinkedList<Integer> currentStepTargets) {
        LinkedList<Integer> transformedTargets = new LinkedList<>();
        for (int currentStepTarget : currentStepTargets) {
            transformedTargets.addAll(transformCurrentStepTarget(data, currentStepTarget, targetChainStep));
        }
        Integer maxRandomTargets = data.getComponent(targetChainStep, Components.Target.TARGET_RANDOM);
        if (maxRandomTargets != null) {
            // TODO: Use random game-tools interface?
            Collections.shuffle(transformedTargets);
            while (transformedTargets.size() > maxRandomTargets) {
                transformedTargets.removeLast();
            }
        }
        return transformedTargets;
    }

    private static LinkedList<Integer> transformCurrentStepTarget(EntityData data, int currentStepTarget, int targetChainStep) {
        LinkedList<Integer> transformedTargets = new LinkedList<>();
        if (data.hasComponent(targetChainStep, Components.Target.TARGET_OWNER)) {
            transformedTargets.add(data.getComponent(currentStepTarget, Components.OWNED_BY));
        } else if (data.hasComponent(targetChainStep, Components.Target.TARGET_OPPONENT)) {
            transformedTargets.add(data.getComponent(currentStepTarget, Components.NEXT_PLAYER));
        } else if (data.hasComponent(targetChainStep, Components.Target.TARGET_CASTER)) {
            transformedTargets.add(SpellUtil.getCaster(data, currentStepTarget));
        } else {
            transformedTargets.add(currentStepTarget);
        }
        return transformedTargets;
    }
}
