package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;

public class TriggerEffectImpactEvent extends Event {

    public final int source;
    public final int target;
    public final int effect;

    public TriggerEffectImpactEvent(int source, int target, int effect) {
        this.source = source;
        this.target = target;
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "TriggerEffectImpactEvent{" + "source=" + source + ", target=" + target + ", effect=" + effect + '}';
    }
}