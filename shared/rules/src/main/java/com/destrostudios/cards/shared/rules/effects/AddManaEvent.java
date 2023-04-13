package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class AddManaEvent extends Event {

    public final int player, manaAmount;

    public AddManaEvent(int player, int manaAmount) {
        super(EventType.ADD_MANA);
        this.player = player;
        this.manaAmount = manaAmount;
    }

    @Override
    public String toString() {
        return "AddManaEvent{" + "player=" + player + ", manaAmount=" + manaAmount + '}';
    }
}
