package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.effects.TriggerEvent;

public class TriggerUtil {

    public static void triggerIfPossible(EntityData data, int[] triggers, int source, int[] targets, EventQueue<GameContext> events) {
        if (triggers != null) {
            for (int trigger : triggers) {
                if (ConditionUtil.isConditionFulfilled(data, trigger, source, targets)) {
                    events.fire(new TriggerEvent(trigger, source, targets));
                }
            }
        }
    }
}
