package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class RemoveCardFromHandEvent extends Event {
    public final int card;

    public RemoveCardFromHandEvent(int card) {
        super(EventType.REMOVE_CARD_FROM_HAND);
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromHandEvent{" + "card=" + card + '}';
    }
}
