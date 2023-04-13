package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class AddCardToBoardEvent extends Event {

    public final int card;

    public AddCardToBoardEvent(int card) {
        super(EventType.ADD_CARD_TO_BOARD);
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToBoardEvent{" + "card=" + card + '}';
    }
}
