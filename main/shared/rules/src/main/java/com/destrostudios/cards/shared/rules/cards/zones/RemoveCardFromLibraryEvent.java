package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;

public class RemoveCardFromLibraryEvent extends Event {
    public final int card;

    public RemoveCardFromLibraryEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromLibraryEvent{" + "card=" + card + '}';
    }
}
