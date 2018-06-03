package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class SetHealthEvent extends Event {

    public final int target, health;

    public SetHealthEvent(int target, int health) {
        this.target = target;
        this.health = health;
    }

    @Override
    public String toString() {
        return "SetHealthEvent{" + "target=" + target + ", health=" + health + '}';
    }
}
