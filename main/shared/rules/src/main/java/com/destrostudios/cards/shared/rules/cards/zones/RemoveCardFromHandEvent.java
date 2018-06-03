package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class RemoveCardFromHandEvent extends Event {
    public final int card;

    public RemoveCardFromHandEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromHandEvent{" + "card=" + card + '}';
    }
}
