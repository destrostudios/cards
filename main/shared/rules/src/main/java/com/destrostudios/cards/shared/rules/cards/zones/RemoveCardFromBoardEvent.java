package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class RemoveCardFromBoardEvent extends Event {

    public final int card;

    public RemoveCardFromBoardEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromBoardEvent{" + "card=" + card + '}';
    }
}
