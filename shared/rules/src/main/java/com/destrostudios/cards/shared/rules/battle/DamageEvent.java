package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class DamageEvent extends Event {

    public int source, target, damage;

    public DamageEvent(int source, int target, int damage) {
        super(EventType.DAMAGE);
        this.source = source;
        this.target = target;
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "DamageEvent{" + "source=" + source+  ", target=" + target + ", damage=" + damage + '}';
    }
}
