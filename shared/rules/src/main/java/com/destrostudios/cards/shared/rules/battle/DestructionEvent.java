package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class DestructionEvent extends Event {

    public int target;

    public DestructionEvent(int target) {
        super(EventType.DESTRUCTION);
        this.target = target;
    }

    @Override
    public String toString() {
        return "DestructionEvent{" + "target=" + target + '}';
    }
}
