package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class SetManaEvent extends Event {

    public final int player, manaAmount;

    public SetManaEvent(int player, int manaAmount) {
        super(EventType.SET_MANA);
        this.player = player;
        this.manaAmount = manaAmount;
    }

    @Override
    public String toString() {
        return "SetManaEvent{" + "player=" + player + ", manaAmount=" + manaAmount + '}';
    }
}
