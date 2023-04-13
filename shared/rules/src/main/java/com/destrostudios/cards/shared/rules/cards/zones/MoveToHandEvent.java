package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class MoveToHandEvent extends Event {
    public final int card;

    public MoveToHandEvent(int card) {
        super(EventType.MOVE_TO_HAND);
        this.card = card;
    }

    @Override
    public String toString() {
        return "MoveToHandEvent{" + "card=" + card + '}';
    }
}
