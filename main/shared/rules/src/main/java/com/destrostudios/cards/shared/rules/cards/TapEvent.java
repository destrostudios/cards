package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 * @author Philipp
 */
public class TapEvent extends Event {

    public final int card;

    public TapEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "TapCardEvent{" + "card=" + card + '}';
    }
}
