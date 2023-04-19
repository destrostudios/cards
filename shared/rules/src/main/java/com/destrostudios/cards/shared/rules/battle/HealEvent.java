package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class HealEvent extends Event {

    public int source, target, heal;

    public HealEvent(int source, int target, int heal) {
        super(EventType.HEAL);
        this.source = source;
        this.target = target;
        this.heal = heal;
    }

    @Override
    public String toString() {
        return "HealEvent{" + "source=" + source + ", target=" + target + ", heal=" + heal + '}';
    }
}
