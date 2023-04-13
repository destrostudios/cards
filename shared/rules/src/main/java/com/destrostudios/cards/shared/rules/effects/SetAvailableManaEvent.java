package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class SetAvailableManaEvent extends Event {

    public final int player, manaAmount;

    public SetAvailableManaEvent(int player, int manaAmount) {
        super(EventType.SET_AVAILABLE_MANA);
        this.player = player;
        this.manaAmount = manaAmount;
    }

    @Override
    public String toString() {
        return "SetAvailableManaEvent{" + "player=" + player + ", manaAmount=" + manaAmount + '}';
    }
}
