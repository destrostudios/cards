package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class AddCardToBoardEvent extends Event {

    public final int card;

    public AddCardToBoardEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToBoardEvent{" + "card=" + card + '}';
    }
}
