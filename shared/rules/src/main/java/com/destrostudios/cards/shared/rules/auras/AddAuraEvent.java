package com.destrostudios.cards.shared.rules.auras;

import com.destrostudios.cards.shared.events.Event;

public class AddAuraEvent extends Event {

    public final int target;
    public final int aura;

    public AddAuraEvent(int target, int aura) {
        this.target = target;
        this.aura = aura;
    }

    @Override
    public String toString() {
        return "AddAuraEvent{" + "target=" + target + ", aura=" + aura + '}';
    }
}
