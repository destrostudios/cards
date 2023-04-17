package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.effects.TriggerIfPossibleEvent;

public class TriggerUtil {

    public static void trigger(int[] triggers, int source, int[] targets, EventQueue<GameContext> events) {
        if (triggers != null) {
            for (int trigger : triggers) {
                events.fire(new TriggerIfPossibleEvent(source, targets, trigger));
            }
        }
    }
}
