package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class DeclareAttackEvent extends Event {

    public int source, target;

    public DeclareAttackEvent(int source, int target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        return "DeclareAttackEvent{" + "source=" + source + ", target=" + target + '}';
    }

}
