package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class TriggerIfPossibleEvent extends Event {

    public final int source;
    public final int[] targets;
    public final int trigger;

    public TriggerIfPossibleEvent(int source, int[] targets, int trigger) {
        super(EventType.TRIGGER_IF_POSSIBLE);
        this.source = source;
        this.targets = targets;
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return "TriggerIfPossibleEvent{" + "source=" + source + ", targets=" + targets + ", trigger=" + trigger + '}';
    }
}
