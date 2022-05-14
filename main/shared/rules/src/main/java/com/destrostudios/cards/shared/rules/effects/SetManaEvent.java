package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;

public class SetManaEvent extends Event {

    public final int player, manaAmount;

    public SetManaEvent(int player, int manaAmount) {
        this.player = player;
        this.manaAmount = manaAmount;
    }

    @Override
    public String toString() {
        return "SetManaEvent{" + "player=" + player + ", manaAmount=" + manaAmount + '}';
    }
}
