package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class RemoveCardFromBoardEvent extends Event {

    public final int card;

    public RemoveCardFromBoardEvent(int card) {
        super(EventType.REMOVE_CARD_FROM_BOARD);
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromBoardEvent{" + "card=" + card + '}';
    }
}
