package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

public class BuffUtil {

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
                if (ConditionUtil.areConditionsFulfilled(data, aura, source, targets)) {
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
            value = buffModifier.getModifiedValue(data, buff, value);
        }
        return value;
    }

    @AllArgsConstructor
    static class SimpleAdditionBuffModifier implements BuffModifier {

        private ComponentDefinition<Integer> component;

        public int getModifiedValue(EntityData data, int buff, int value) {
            Integer additionalValue = data.getComponent(buff, component);
            if (additionalValue != null) {
                return value + additionalValue;
            }
            return value;
        }
    }

    interface BuffModifier {
        int getModifiedValue(EntityData data, int buff, int value);
    }
}
