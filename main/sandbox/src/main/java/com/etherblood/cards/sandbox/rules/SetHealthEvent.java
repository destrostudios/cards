package com.etherblood.cards.sandbox.rules;

import com.etherblood.cards.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class SetHealthEvent extends TriggeredEvent {

    public int target, health;

    public SetHealthEvent(int target, int health) {
        this.target = target;
        this.health = health;
    }
}
