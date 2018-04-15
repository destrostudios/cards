package com.etherblood.cards.rules.battle;

import com.etherblood.cards.events.ResponseEvent;

/**
 *
 * @author Philipp
 */
public class SetHealthEvent extends ResponseEvent {

    public int target, health;

    public SetHealthEvent(int target, int health) {
        this.target = target;
        this.health = health;
    }
}
