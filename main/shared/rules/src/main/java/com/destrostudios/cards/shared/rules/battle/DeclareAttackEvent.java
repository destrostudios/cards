package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.ActionEvent;

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
