package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class AddCardToHandEvent extends Event {
    public final int card;

    public AddCardToHandEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "AddCardToHandEvent{" + "card=" + card + '}';
    }
}
