package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class HealEvent extends Event {

    public int target, heal;

    public HealEvent(int target, int heal) {
        super(EventType.HEAL);
        this.target = target;
        this.heal = heal;
    }

    @Override
    public String toString() {
        return "HealEvent{" + "target=" + target + ", heal=" + heal + '}';
    }
}
