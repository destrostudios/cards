package com.etherblood.cards.rules.battle;

import com.etherblood.cards.events.ResponseEvent;

/**
 *
 * @author Philipp
 */
public class DamageEvent extends ResponseEvent {

    public int target, damage;

    public DamageEvent(int target, int damage) {
        this.target = target;
        this.damage = damage;
    }
}
