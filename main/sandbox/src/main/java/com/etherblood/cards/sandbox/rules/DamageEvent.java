package com.etherblood.cards.sandbox.rules;

import com.etherblood.cards.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class DamageEvent extends TriggeredEvent {

    public int target, damage;

    public DamageEvent(int target, int damage) {
        this.target = target;
        this.damage = damage;
    }
}
