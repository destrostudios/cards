package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.events.Event;

public class RemoveBuffEvent extends Event {

    public final int target;
    public final int buff;

    public RemoveBuffEvent(int target, int buff) {
        this.target = target;
        this.buff = buff;
    }

    @Override
    public String toString() {
        return "RemoveBuffEvent{" + "target=" + target + ", buff=" + buff + '}';
    }
}
