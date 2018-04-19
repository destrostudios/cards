package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.ResponseEvent;

/**
 *
 * @author Philipp
 */
public class RemoveCardFromHandEvent extends ResponseEvent {
    public int card;

    public RemoveCardFromHandEvent(int card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "RemoveCardFromHandEvent{" + "card=" + card + '}';
    }
}
