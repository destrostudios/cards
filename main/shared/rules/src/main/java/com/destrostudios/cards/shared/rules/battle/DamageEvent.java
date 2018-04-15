package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.ResponseEvent;

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
