package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.effects.TriggerEffectTriggerIfPossibleEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class EffectTriggerUtil {

    public static void trigger(int[] effectTriggers, int source, int[] targets, EventQueue events, NetworkRandom random) {
        if (effectTriggers != null) {
            for (int effectTrigger : effectTriggers) {
                events.fire(new TriggerEffectTriggerIfPossibleEvent(source, targets, effectTrigger), random);
            }
        }
    }
}
