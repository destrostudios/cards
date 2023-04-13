package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class MoveToLibraryEvent extends Event {
    public final int card;

    public MoveToLibraryEvent(int card) {
        super(EventType.MOVE_TO_LIBRARY);
        this.card = card;
    }

    @Override
    public String toString() {
        return "MoveToLibraryEvent{" + "card=" + card + '}';
    }
}
