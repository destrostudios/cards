package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.expressions.ExpressionContextProvider;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BuffUtil {

    public static void add(EntityData data, int entity, int buff) {
        if (data.hasComponent(buff, Components.Stats.SET_HEALTH)) {
            data.removeComponent(entity, Components.Stats.DAMAGED);
            data.removeComponent(entity, Components.Stats.BONUS_DAMAGED);
        }
        ArrayUtil.add(data, entity, Components.BUFFS, buff);
    }

    public static void remove(EntityData data, int entity, int buff) {
        ArrayUtil.remove(data, entity, Components.BUFFS, buff);
        // TODO: Remove buff entity if it was an evaluated copy? Overkill for now?
    }

    public static int createEvaluatedBuffCopy(EntityData data, int buff, ExpressionContextProvider expressionContextProvider) {
        int buffCopy = data.createEntity();
        // TODO: Good enough for now
        for (ComponentDefinition component : Components.ALL) {
            Object value = data.getComponent(buff, component);
            if (value != null) {
                if ((component == Components.Cost.BONUS_MANA_COST)
                 || (component == Components.Cost.SET_MANA_COST)
                 || (component == Components.Stats.BONUS_ATTACK)
                 || (component == Components.Stats.SET_ATTACK)
                 || (component == Components.Stats.BONUS_HEALTH)
                 || (component == Components.Stats.SET_HEALTH)
                ) {
                    value = Expressions.evaluate(value.toString(), Expressions.getContext_Provider(data, expressionContextProvider)).toString();
                }
                data.setComponent(buffCopy, component, value);
            }
        }
        return buffCopy;
    }

    static BuffUtil.BuffCalculationResult calculateWithBuffs(EntityData data, int entity, ComponentDefinition<Integer> initialValueComponent, BuffUtil.BuffModifier buffModifier) {
        Integer initialValue = data.getComponent(entity, initialValueComponent);
        if (initialValue != null) {
            BuffCalculationResult result = new BuffCalculationResult(initialValue, 0);
            IntList buffs = getAffectingBuffs(data, entity);
            for (int buff : buffs) {
                buffModifier.modifyValue(data, entity, buff, result);
            }
            return result;
        }
        return null;
    }

    public static IntList getAffectingBuffs(EntityData data, int target) {
        IntList affectingBuffs = new IntList();
        // Direct buffs
        IntList buffs = data.getComponent(target, Components.BUFFS);
        if (buffs != null) {
            affectingBuffs.addAll(buffs);
        }
        // Auras
        int[] targets = new int[] { target };
        for (int source : data.list(Components.AURAS)) {
            int[] auras = data.getComponent(source, Components.AURAS);
            for (int aura : auras) {
                if (ConditionUtil.isConditionFulfilled(data, aura, source, targets)) {
                    int buff = data.getComponent(aura, Components.Aura.AURA_BUFF);
                    affectingBuffs.add(buff);
                }
            }
        }
        return affectingBuffs;
    }

    @AllArgsConstructor
    static class SimpleBuffModifier implements BuffModifier {

        private ComponentDefinition<String> bonusComponent;
        private ComponentDefinition<String> setComponent;

        public void modifyValue(EntityData data, int target, int buff, BuffCalculationResult result) {
            Integer setValue = getValue(data, target, buff, setComponent);
            if (setValue != null) {
                result.baseValue = setValue;
                result.bonusValue = 0;
            } else {
                Integer bonusValue = getValue(data, target, buff, bonusComponent);
                if (bonusValue != null) {
                    result.bonusValue += bonusValue;
                }
            }
        }

        private Integer getValue(EntityData data, int target, int buff, ComponentDefinition<String> component) {
            String expression = data.getComponent(buff, component);
            if (expression != null) {
                int source = data.getComponent(buff, Components.SOURCE);
                return Expressions.evaluate(expression, Expressions.getContext_Source_Target(data, source, target));
            }
            return null;
        }
    }

    interface BuffModifier {
        void modifyValue(EntityData data, int target, int buff, BuffCalculationResult result);
    }

    @AllArgsConstructor
    @Getter
    public static class BuffCalculationResult {

        private int baseValue;
        private int bonusValue;

        public int getEffectiveValue() {
            return baseValue + bonusValue;
        }
    }
}
