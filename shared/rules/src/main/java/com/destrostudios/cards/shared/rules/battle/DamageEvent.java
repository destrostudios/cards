package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

/**
 *
 * @author Philipp
 */
public class DamageEvent extends Event {

    public int target, damage;

    public DamageEvent(int target, int damage) {
        super(EventType.DAMAGE);
        this.target = target;
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "DamageEvent{" + "target=" + target + ", damage=" + damage + '}';
    }
}
