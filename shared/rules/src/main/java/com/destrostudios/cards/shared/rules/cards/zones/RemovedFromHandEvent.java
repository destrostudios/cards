package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class RemovedFromHandEvent extends Event {
    public final int card;

    public RemovedFromHandEvent(int card) {
        super(EventType.REMOVED_FROM_HAND);
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemovedFromHandEvent{" + "card=" + card + '}';
    }
}
