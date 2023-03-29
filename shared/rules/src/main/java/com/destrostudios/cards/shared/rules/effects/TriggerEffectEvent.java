package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;

public class TriggerEffectEvent extends Event {

    public final int source;
    public final int[] targets;
    public final int effect;

    public TriggerEffectEvent(int source, int[] targets, int effect) {
        this.source = source;
        this.targets = targets;
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "TriggerEffectEvent{" + "source=" + source + ", targets=" + targets + ", effect=" + effect + '}';
    }
}
