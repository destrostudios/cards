package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

public class DestructionEvent extends Event {

    final int target;

    public DestructionEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "DestructionEvent{" + "target=" + target + '}';
    }
}
