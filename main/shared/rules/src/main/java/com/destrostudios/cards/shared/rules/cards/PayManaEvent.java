package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

public class PayManaEvent extends Event {

    public final int player;
    public final int manaAmount;

    public PayManaEvent(int player, int manaAmount) {
        this.player = player;
        this.manaAmount = manaAmount;
    }

    @Override
    public String toString() {
        return "PayCostEvent{" + "player=" + player + ", manaAmount=" + manaAmount + '}';
    }
}
