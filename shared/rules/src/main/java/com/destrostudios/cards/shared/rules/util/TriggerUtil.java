package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.effects.TriggerIfPossibleEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class TriggerUtil {

    public static void trigger(int[] triggers, int source, int[] targets, EventQueue events, NetworkRandom random) {
        if (triggers != null) {
            for (int trigger : triggers) {
                events.fire(new TriggerIfPossibleEvent(source, targets, trigger), random);
            }
        }
    }
}