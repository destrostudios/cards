package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.ResponseEvent;

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

    @Override
    public String toString() {
        return "SetHealthEvent{" + "target=" + target + ", health=" + health + '}';
    }
}
