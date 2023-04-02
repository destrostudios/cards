package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class BuffUtil {

    public static void add(EntityData data, int entity, int buff) {
        if (data.hasComponent(buff, Components.Stats.SET_HEALTH)) {
            data.removeComponent(entity, Components.Stats.DAMAGED);
            data.removeComponent(entity, Components.Stats.BONUS_DAMAGED);
        }
        ArrayUtil.add(data, entity, Components.BUFFS, buff);
    }

    public static int createEvaluatedBuffCopy(EntityData data, int buff, int source, int target) {
        int buffCopy = data.createEntity();
        for (ComponentDefinition component : Components.ALL) {
            Object value = data.getComponent(buff, component);
            // TODO: Good enough for now
            if ((component == Components.Cost.BONUS_MANA_COST)
             || (component == Components.Cost.SET_MANA_COST)
             || (component == Components.Stats.BONUS_ATTACK)
             || (component == Components.Stats.SET_ATTACK)
             || (component == Components.Stats.BONUS_HEALTH)
             || (component == Components.Stats.SET_HEALTH)
            ) {
                value = Expressions.evaluate(data, value.toString(), source, target).toString();
            }
            data.setComponent(buffCopy, component, value);
        }
        return buffCopy;
    }

    static BuffUtil.BuffCalculationResult calculateWithBuffs(EntityData data, int entity, ComponentDefinition<Integer> initialValueComponent, BuffUtil.BuffModifier buffModifier) {
        Integer initialValue = data.getComponent(entity, initialValueComponent);
        if (initialValue != null) {
            BuffCalculationResult result = new BuffCalculationResult(initialValue, 0);
            List<Integer> buffs = getAffectingBuffs(data, entity);
            for (int buff : buffs) {
                buffModifier.modifyValue(data, entity, buff, result);
            }
            return result;
        }
        return null;
    }

    public static List<Integer> getAffectingBuffs(EntityData data, int entity) {
        LinkedList<Integer> affectingBuffs = new LinkedList<>();
        // Direct buffs
        int[] buffs = data.getComponent(entity, Components.BUFFS);
        if (buffs != null) {
            for (int buff : buffs) {
                affectingBuffs.add(buff);
            }
        }
        // Auras
        int[] targets = new int[] { entity };
        for (int source : data.query(Components.AURAS).list()) {
            int[] auras = data.getComponent(source, Components.AURAS);
            for (int aura : auras) {
                int buff = data.getComponent(aura, Components.Aura.AURA_BUFF);
                if (ConditionUtil.isConditionFulfilled(data, aura, source, targets)) {
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
                    result.bonusValue = bonusValue;
                }
            }
        }

        private Integer getValue(EntityData data, int target, int buff, ComponentDefinition<String> component) {
            String expression = data.getComponent(buff, component);
            if (expression != null) {
                int source = data.getComponent(buff, Components.SOURCE);
                return Expressions.evaluate(data, expression, source, target);
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
