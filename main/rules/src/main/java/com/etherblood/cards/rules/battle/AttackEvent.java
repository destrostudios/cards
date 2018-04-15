package com.etherblood.cards.rules.battle;

import com.etherblood.cards.events.ResponseEvent;

/**
 *
 * @author Philipp
 */
public class AttackEvent extends ResponseEvent {

    public int source, target;

    public AttackEvent(int source, int target) {
        this.source = source;
        this.target = target;
    }

}
