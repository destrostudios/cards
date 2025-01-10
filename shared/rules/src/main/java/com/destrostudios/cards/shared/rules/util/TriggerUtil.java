package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.effects.EffectOptions;
import com.destrostudios.cards.shared.rules.effects.TriggerEvent;

public class TriggerUtil {

    public static void triggerIfPossible(EntityData data, int[] triggers, int source, int[] targets, EffectOptions options, EventQueue<GameContext> events) {
        for (int trigger : triggers) {
            triggerIfPossible(data, trigger, source, targets, options, events);
        }
    }

    public static void triggerIfPossible(EntityData data, int trigger, int source, int[] targets, EffectOptions options, EventQueue<GameContext> events) {
        if (ConditionUtil.isConditionFulfilled(data, trigger, source, targets)) {
            events.fire(new TriggerEvent(trigger, source, targets, options));
        }
    }

    public static void triggerDelayed(EntityData data, int source, int target, Components.TriggerDelayed triggerDelayed) {
        Components.TriggeredTrigger[] oldTriggeredTriggers = data.getComponent(source, triggerDelayed.getTriggeredTriggersComponent());
        if (oldTriggeredTriggers == null) {
            oldTriggeredTriggers = new Components.TriggeredTrigger[0];
        }
        Components.TriggeredTrigger[] newTriggeredTriggers = new Components.TriggeredTrigger[oldTriggeredTriggers.length + triggerDelayed.getTriggers().length];
        System.arraycopy(oldTriggeredTriggers, 0, newTriggeredTriggers, 0, oldTriggeredTriggers.length);
        for (int i = 0; i < triggerDelayed.getTriggers().length; i++) {
            newTriggeredTriggers[oldTriggeredTriggers.length + i] = new Components.TriggeredTrigger(triggerDelayed.getTriggers()[i], target);
        }
        data.setComponent(source, triggerDelayed.getTriggeredTriggersComponent(), newTriggeredTriggers);
    }
}
