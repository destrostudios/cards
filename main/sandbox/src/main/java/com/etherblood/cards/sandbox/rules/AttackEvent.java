package com.etherblood.cards.sandbox.rules;

import com.etherblood.cards.events.ActionEvent;

/**
 *
 * @author Philipp
 */
public class AttackEvent extends ActionEvent {

    public int source, target;

    public AttackEvent(int source, int target) {
        this.source = source;
        this.target = target;
    }

}
