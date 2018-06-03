package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class RemoveCardFromGraveyardEvent extends Event {
    public final int card;

    public RemoveCardFromGraveyardEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromLibraryEvent{" + "card=" + card + '}';
    }
}
