package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class AddBuffEvent extends Event {

    public final int buff;
    public final int target;

    public AddBuffEvent(int target, int buff) {
        super(EventType.ADD_BUFF);
        this.target = target;
        this.buff = buff;
    }

    @Override
    public String toString() {
        return "AddBuffEvent{" + "target=" + target + ", buff=" + buff + '}';
    }
}
