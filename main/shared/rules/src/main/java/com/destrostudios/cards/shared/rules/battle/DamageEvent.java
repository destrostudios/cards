package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class DamageEvent extends Event {

    public int target, damage;

    // Used by serializer
    private DamageEvent() {
        this(0, 0);
    }

    public DamageEvent(int target, int damage) {
        this.target = target;
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "DamageEvent{" + "target=" + target + ", damage=" + damage + '}';
    }
}
