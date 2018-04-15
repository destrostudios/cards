package com.etherblood.cards.rules.battle;

import com.etherblood.cards.events.ActionEvent;

/**
 *
 * @author Philipp
 */
public class DeclareAttackEvent extends ActionEvent {

    public int source, target;

    public DeclareAttackEvent(int source, int target) {
        this.source = source;
        this.target = target;
    }

}
