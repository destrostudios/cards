package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class SetDamagedEvent extends Event {

    public final int target, damaged;

    public SetDamagedEvent(int target, int damaged) {
        this.target = target;
        this.damaged = damaged;
    }

    @Override
    public String toString() {
        return "SetDamagedEvent{" + "target=" + target + ", damaged=" + damaged + '}';
    }
}
