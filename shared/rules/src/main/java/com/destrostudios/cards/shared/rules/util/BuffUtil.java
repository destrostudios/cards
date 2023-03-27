package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

public class BuffUtil {

    public static int createEvaluatedBuffCopy(EntityData data, int buff, int source, int target) {
        int buffCopy = data.createEntity();
        for (ComponentDefinition component : Components.ALL) {
            Object value = data.getComponent(buff, component);
            if ((component == Components.Cost.BONUS_MANA_COST) || (component == Components.Stats.BONUS_ATTACK) || (component == Components.Stats.BONUS_HEALTH)) {
                value = Expressions.evaluate(data, value.toString(), source, target).toString();
            }
            data.setComponent(buffCopy, component, value);
        }
        return buffCopy;
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

    static Integer modifyViaBuffs(EntityData data, int entity, int initialValue, BuffModifier buffModifier) {
        int value = initialValue;
        List<Integer> buffs = BuffUtil.getAffectingBuffs(data, entity);
        for (int buff : buffs) {
            value = buffModifier.getModifiedValue(data, entity, buff, value);
        }
        return value;
    }

    @AllArgsConstructor
    static class SimpleAdditionBuffModifier implements BuffModifier {

        private ComponentDefinition<String> component;

        public int getModifiedValue(EntityData data, int target, int buff, int value) {
            String additionValueExpression = data.getComponent(buff, component);
            if (additionValueExpression != null) {
                int source = data.getComponent(buff, Components.SOURCE);
                int additionValue = Expressions.evaluate(data, additionValueExpression, source, target);
                return value + additionValue;
            }
            return value;
        }
    }

    interface BuffModifier {
        int getModifiedValue(EntityData data, int target, int buff, int value);
    }
}
