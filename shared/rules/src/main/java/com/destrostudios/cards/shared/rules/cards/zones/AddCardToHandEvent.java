package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class AddCardToHandEvent extends Event {
    public final int card;

    public AddCardToHandEvent(int card) {
        super(EventType.ADD_CARD_TO_HAND);
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToHandEvent{" + "card=" + card + '}';
    }
}
