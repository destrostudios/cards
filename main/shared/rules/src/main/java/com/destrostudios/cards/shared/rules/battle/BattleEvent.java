package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class BattleEvent extends Event {

    public int source, target;

    public BattleEvent(int source, int target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        return "BattleEvent{" + "source=" + source + ", target=" + target + '}';
    }

}
