package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.util.MixedManaAmount;

public class PayManaEvent extends Event {

    public final int player;
    public final MixedManaAmount mixedManaAmount;

    public PayManaEvent(int player, MixedManaAmount mixedManaAmount) {
        this.player = player;
        this.mixedManaAmount = mixedManaAmount;
    }

    @Override
    public String toString() {
        return "PayCostEvent{" + "player=" + player + ", mixedManaAmount=" + mixedManaAmount + '}';
    }
}
