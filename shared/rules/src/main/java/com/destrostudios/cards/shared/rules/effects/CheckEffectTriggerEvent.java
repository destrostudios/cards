package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;

public class CheckEffectTriggerEvent extends Event {

    public final int source;
    public final int[] targets;
    public final int effectTrigger;

    public CheckEffectTriggerEvent(int source, int[] targets, int effectTrigger) {
        this.source = source;
        this.targets = targets;
        this.effectTrigger = effectTrigger;
    }

    @Override
    public String toString() {
        return "CheckEffectTriggerEvent{" + "source=" + source + ", targets=" + targets + ", effectTrigger=" + effectTrigger + '}';
    }
}
