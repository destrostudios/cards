package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class DiscardEvent extends Event {

    public int card;

    public DiscardEvent(int card) {
        super(EventType.DISCARD);
        this.card = card;
    }

    @Override
    public String toString() {
        return "DiscardCardEvent{" + "card=" + card + '}';
    }
}
