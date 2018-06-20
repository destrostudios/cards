package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;

public class AddManaEvent extends Event {

    public final int player, manaEntity;

    public AddManaEvent(int player, int manaEntity) {
        this.player = player;
        this.manaEntity = manaEntity;
    }

    @Override
    public String toString() {
        return "AddManaEvent{" + "player=" + player + ", manaEntity=" + manaEntity + '}';
    }
}
