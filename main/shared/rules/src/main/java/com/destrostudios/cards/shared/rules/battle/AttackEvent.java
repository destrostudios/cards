package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class AttackEvent extends Event {

    public int source, target;

    // Used by serializer
    private AttackEvent() {
        this(0, 0);
    }

    public AttackEvent(int source, int target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        return AttackEvent.class.getSimpleName() + "{source=" + source + ", target=" + target + '}';
    }

}
