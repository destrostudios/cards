package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.ResponseEvent;

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

    @Override
    public String toString() {
        return "AttackEvent{" + "source=" + source + ", target=" + target + '}';
    }

}
