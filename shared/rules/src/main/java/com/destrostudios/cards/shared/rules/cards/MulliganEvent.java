package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

import java.util.Arrays;

public class MulliganEvent extends Event {

    public int[] cards;

    // Used by serializer
    private MulliganEvent() {
        this(null);
    }

    public MulliganEvent(int[] cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "MulliganEvent{" + "cards=" + cards + '}';
    }

    // Used by game-tools bot

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MulliganEvent that = (MulliganEvent) o;
        return Arrays.equals(cards, that.cards);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cards);
    }
}
