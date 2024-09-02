package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class BattleEvent extends Event {

    public int source, target;

    public BattleEvent(int source, int target) {
        super(EventType.BATTLE);
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        return BattleEvent.class.getSimpleName() + "{source=" + source + ", target=" + target + '}';
    }
}
