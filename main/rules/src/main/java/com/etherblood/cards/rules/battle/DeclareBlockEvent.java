package com.etherblood.cards.rules.battle;

import com.etherblood.cards.events.ActionEvent;

/**
 *
 * @author Philipp
 */
public class DeclareBlockEvent extends ActionEvent {

    public int source, target;

    public DeclareBlockEvent(int source, int target) {
        this.source = source;
        this.target = target;
    }

}
