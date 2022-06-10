package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

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
}
