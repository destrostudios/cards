package com.destrostudios.cards.shared.rules.auras;

import com.destrostudios.cards.shared.events.Event;

public class RemoveAuraEvent extends Event {

    public final int target;
    public final int aura;

    public RemoveAuraEvent(int target, int aura) {
        this.target = target;
        this.aura = aura;
    }

    @Override
    public String toString() {
        return "RemoveAuraEvent{" + "target=" + target + ", aura=" + aura + '}';
    }
}
