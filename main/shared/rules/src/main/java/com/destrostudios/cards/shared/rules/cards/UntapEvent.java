package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 * @author Philipp
 */
public class UntapEvent extends Event {

    public final int card;

    public UntapEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "UntapCardEvent{" + "card=" + card + '}';
    }
}