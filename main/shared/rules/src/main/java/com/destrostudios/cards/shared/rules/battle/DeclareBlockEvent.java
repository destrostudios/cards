package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.ActionEvent;

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

    @Override
    public String toString() {
        return "DeclareBlockEvent{" + "source=" + source + ", target=" + target + '}';
    }

}
