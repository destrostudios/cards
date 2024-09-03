package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.AdvancedPrefilter;
import com.destrostudios.cards.shared.rules.SimpleTarget;
import com.destrostudios.cards.shared.rules.expressions.ExpressionContextProvider;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.function.IntConsumer;

public class TargetUtil {

    public static IntList getAffectedTargets(EntityData data, int[] targetDefinitions, int source, int[] targets, ExpressionContextProvider expressionContextProvider, NetworkRandom random) {
        // TODO: Use Set so entities are not affected multiple times? Dependent on effect/context? (here and in the methods below)
        IntList affectedTargets = new IntList();
        for (int targetDefinition : targetDefinitions) {
            affectedTargets.addAll(getAffectedTargets(data, targetDefinition, source, targets, expressionContextProvider, random));
        }
        return affectedTargets;
    }

    private static IntList getAffectedTargets(EntityData data, int targetDefinition, int source, int[] targets, ExpressionContextProvider expressionContextProvider, NetworkRandom random) {
        IntList affectedTargets = new IntList();
        if (isFulfillingPrefilters_Source(data, source, targetDefinition)) {
            SimpleTarget[] simpleTargets = data.getComponent(targetDefinition, Components.Target.TARGET_SIMPLE);
            if (simpleTargets != null) {
                for (SimpleTarget simpleTarget : simpleTargets) {
                    addSimpleTarget(data, source, targets, simpleTarget, affectedTargets::add);
                }
            }
            String targetExpression = data.getComponent(targetDefinition, Components.Target.TARGET_CUSTOM);
            if (targetExpression != null) {
                int[] evaluatedTargets = Expressions.evaluateEntities(targetExpression, Expressions.getContext_Provider(data, expressionContextProvider));
                for (int target : evaluatedTargets) {
                    affectedTargets.add(target);
                }
            }
            String targetAllCondition = data.getComponent(targetDefinition, Components.Target.TARGET_ALL);
            if (targetAllCondition != null) {
                Components.Prefilters targetPrefilters = data.getComponent(targetDefinition, Components.Target.TARGET_PREFILTERS);
                affectedTargets.addAll(getAllConditionTargets(data, source, targetPrefilters, targetAllCondition));
            }
            String maxRandomTargetsExpression = data.getComponent(targetDefinition, Components.Target.TARGET_RANDOM);
            if (maxRandomTargetsExpression != null) {
                int maxRandomTargets = Expressions.evaluate(maxRandomTargetsExpression, Expressions.getContext_Provider(data, expressionContextProvider));
                while (affectedTargets.size() > maxRandomTargets) {
                    affectedTargets.removeAt(random.nextInt(affectedTargets.size()));
                }
            }
        }
        return affectedTargets;
    }

    private static void addSimpleTarget(EntityData data, int source, int[] targets, SimpleTarget simpleTarget, IntConsumer add) {
        switch (simpleTarget) {
            case SOURCE -> {
                add.accept(source);
            }
            case SOURCE_OWNER -> {
                add.accept(data.getComponent(source, Components.OWNED_BY));
            }
            case SOURCE_OWNER_OPPONENT -> {
                int sourceOwner = data.getComponent(source, Components.OWNED_BY);
                add.accept(data.getComponent(sourceOwner, Components.NEXT_PLAYER));
            }
            case SOURCE_DEFAULT_CAST_FROM_HAND_SPELL -> {
                add.accept(SpellUtil.getDefaultCastFromHandSpell(data, source));
            }
            case TARGETS -> {
                for (int target : targets) {
                    add.accept(target);
                }
            }
            case TARGETS_DEFAULT_CAST_FROM_HAND_SPELL -> {
                for (int target : targets) {
                    add.accept(SpellUtil.getDefaultCastFromHandSpell(data, target));
                }
            }
        }
    }

    public static IntList getAllConditionTargets(EntityData data, int source, Components.Prefilters targetPrefilters, String condition) {
        IntList prefilteredTargets = getPrefilteredEntities(data, source, targetPrefilters);
        if (condition.isEmpty()) {
            return prefilteredTargets;
        }
        prefilteredTargets.retain(target -> ConditionUtil.isConditionFulfilled(data, source, new int[] { target }, condition));
        return prefilteredTargets;
    }

    public static IntList getPrefilteredEntities(EntityData data, int source, Components.Prefilters prefilters) {
        return data.listAll(prefilters.getBasicComponents(), entity -> isFulfillingPrefilters_Advanced(data, entity, source, prefilters.getAdvanced()));
    }

    public static boolean isFulfillingPrefilters_Source(EntityData data, int source, int entityWithPrefilters) {
        return isFulfillingPrefilters(data, source, source, entityWithPrefilters, Components.Target.SOURCE_PREFILTERS);
    }

    public static boolean isFulfillingPrefilters_Target(EntityData data, int target, int source, int entityWithPrefilters) {
        return isFulfillingPrefilters(data, target, source, entityWithPrefilters, Components.Target.TARGET_PREFILTERS);
    }

    private static boolean isFulfillingPrefilters(EntityData data, int entity, int source, int entityWithPrefilters, ComponentDefinition<Components.Prefilters> prefiltersComponent) {
        Components.Prefilters prefilters = data.getComponent(entityWithPrefilters, prefiltersComponent);
        if (prefilters != null) {
            return isFulfillingPrefilters_Basic(data, entity, prefilters.getBasicComponents()) && isFulfillingPrefilters_Advanced(data, entity, source, prefilters.getAdvanced());
        }
        return true;
    }

    private static boolean isFulfillingPrefilters_Basic(EntityData data, int entity, ComponentDefinition<?>[] basicComponents) {
        for (ComponentDefinition<?> basicComponent : basicComponents) {
            if (!data.hasComponent(entity, basicComponent)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFulfillingPrefilters_Advanced(EntityData data, int entity, int source, AdvancedPrefilter[] advancedPrefilters) {
        for (AdvancedPrefilter advancedPrefilter : advancedPrefilters) {
            if (!isFulfillingPrefilter_Advanced(data, entity, source, advancedPrefilter)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFulfillingPrefilter_Advanced(EntityData data, int entity, int source, AdvancedPrefilter advancedPrefilter) {
        switch (advancedPrefilter) {
            case CREATURE_OR_NO_CREATURES -> {
                if (data.hasComponent(entity, Components.CREATURE_CARD)) {
                    return true;
                }
                return data.getComponent(entity, Components.Player.CREATURE_ZONE_CARDS).isEmpty();
            }
            case SOURCE -> {
                return entity == source;
            }
            case NOT_SOURCE -> {
                return entity != source;
            }
            case ALLY -> {
                return ConditionUtil.isAlly(data, entity, source);
            }
            case NOT_ALLY -> {
                return !ConditionUtil.isAlly(data, entity, source);
            }
            case DAMAGED -> {
                return StatsUtil.isDamaged(data, entity);
            }
            case NOT_DAMAGED -> {
                return !StatsUtil.isDamaged(data, entity);
            }
            case OWNER -> {
                return entity == data.getComponent(source, Components.OWNED_BY);
            }
            case OPPONENT -> {
                int sourceOwner = data.getComponent(source, Components.OWNED_BY);
                int sourceOpponent = data.getComponent(sourceOwner, Components.NEXT_PLAYER);
                return (entity == sourceOpponent);
            }
            case DEFAULT_CAST_FROM_HAND_SPELL -> {
                return SpellUtil.isDefaultCastFromHandSpell(data, entity);
            }
            case SOURCE_DEFAULT_CAST_FROM_HAND_SPELL -> {
                return SpellUtil.getDefaultCastFromHandSpell(data, source) == entity;
            }
            case SOURCE_HAND -> {
                return hasSourceComponent(data, entity, Components.Zone.HAND);
            }
            case SOURCE_CREATURE_CARD -> {
                return hasSourceComponent(data, entity, Components.CREATURE_CARD);
            }
            case SOURCE_SPELL_CARD -> {
                return hasSourceComponent(data, entity, Components.SPELL_CARD);
            }
            case SOURCE_DRAGON -> {
                return hasSourceComponent(data, entity, Components.Tribe.DRAGON);
            }
            case SOURCE_ALLY -> {
                int entitySource = data.getComponent(entity, Components.SOURCE);
                return ConditionUtil.isAlly(data, entitySource, source);
            }
            case SOURCE_NOT_ALLY -> {
                int entitySource = data.getComponent(entity, Components.SOURCE);
                return !ConditionUtil.isAlly(data, entitySource, source);
            }
        }
        throw new IllegalArgumentException();
    }

    private static boolean hasSourceComponent(EntityData data, int entity, ComponentDefinition<?> component) {
        int entitySource = data.getComponent(entity, Components.SOURCE);
        return data.hasComponent(entitySource, component);
    }
}
