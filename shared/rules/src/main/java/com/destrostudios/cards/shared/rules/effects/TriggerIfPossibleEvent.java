package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;

public class TriggerIfPossibleEvent extends Event {

    public final int source;
    public final int[] targets;
    public final int trigger;

    public TriggerIfPossibleEvent(int source, int[] targets, int trigger) {
        this.source = source;
        this.targets = targets;
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return "TriggerIfPossibleEvent{" + "source=" + source + ", targets=" + targets + ", trigger=" + trigger + '}';
    }
}
