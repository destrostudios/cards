package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.LinkedList;
import java.util.List;

public class TargetUtil {

    public static List<Integer> getAffectedTargets(EntityData data, int[] targetChains, int source, int[] targets) {
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
        int[] conditions = data.getComponent(targetChainStep, Components.Target.TARGET_ALL);
        if (conditions != null) {
            // TODO: Unify
            addAllTargets(data, conditions, source, data.query(Components.OWNED_BY).list(), initialTargets);
            addAllTargets(data, conditions, source, data.query(Components.NEXT_PLAYER).list(), initialTargets);
        }
        return initialTargets;
    }

    private static void addAllTargets(EntityData data, int[] conditions, int source, List<Integer> targetsToCheck, LinkedList<Integer> destinationTargets) {
        for (int target : targetsToCheck) {
            if (ConditionUtil.areConditionsFulfilled(data, conditions, source, new int[] { target })) {
                destinationTargets.add(target);
            }
        }
    }

    private static LinkedList<Integer> transformCurrentStepTargets(EntityData data, int targetChainStep, LinkedList<Integer> currentStepTargets) {
        LinkedList<Integer> transformedTargets = new LinkedList<>();
        for (int currentStepTarget : currentStepTargets) {
            transformedTargets.addAll(transformCurrentStepTarget(data, currentStepTarget, targetChainStep));
        }
        return transformedTargets;
    }

    private static LinkedList<Integer> transformCurrentStepTarget(EntityData data, int currentStepTarget, int targetChainStep) {
        LinkedList<Integer> transformedTargets = new LinkedList<>();
        if (data.hasComponent(targetChainStep, Components.Target.TARGET_OWNER)) {
            transformedTargets.add(data.getComponent(currentStepTarget, Components.OWNED_BY));
        }
        return transformedTargets;
    }
}
