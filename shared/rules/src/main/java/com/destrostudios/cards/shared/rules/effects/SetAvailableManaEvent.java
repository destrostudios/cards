package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;

public class SetAvailableManaEvent extends Event {

    public final int player, manaAmount;

    public SetAvailableManaEvent(int player, int manaAmount) {
        this.player = player;
        this.manaAmount = manaAmount;
    }

    @Override
    public String toString() {
        return "SetAvailableManaEvent{" + "player=" + player + ", manaAmount=" + manaAmount + '}';
    }
}
