package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

public class HealEvent extends Event {

    public int target, heal;

    public HealEvent(int target, int heal) {
        this.target = target;
        this.heal = heal;
    }

    @Override
    public String toString() {
        return "DamageEvent{" + "target=" + target + ", heal=" + heal + '}';
    }
}
