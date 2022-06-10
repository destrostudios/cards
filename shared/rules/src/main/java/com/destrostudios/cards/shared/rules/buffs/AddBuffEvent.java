package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.events.Event;

public class AddBuffEvent extends Event {

    public final int buff;
    public final int target;

    public AddBuffEvent(int target, int buff) {
        this.target = target;
        this.buff = buff;
    }

    @Override
    public String toString() {
        return "AddBuffEvent{" + "target=" + target + ", buff=" + buff + '}';
    }
}
