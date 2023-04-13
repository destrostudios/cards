package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class MoveToGraveyardEvent extends Event {
    public final int card;

    public MoveToGraveyardEvent(int card) {
        super(EventType.MOVE_TO_GRAVEYARD);
        this.card = card;
    }

    @Override
    public String toString() {
        return "MoveToGraveyardEvent{" + "card=" + card + '}';
    }
}
