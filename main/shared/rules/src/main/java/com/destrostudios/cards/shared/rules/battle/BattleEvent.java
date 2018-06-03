package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class BattleEvent extends Event {

    public int source, target;

    // Used by serializer
    private BattleEvent() {
        this(0, 0);
    }

    public BattleEvent(int source, int target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        return BattleEvent.class.getSimpleName() + "{source=" + source + ", target=" + target + '}';
    }

}
