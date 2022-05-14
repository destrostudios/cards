package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

public class PayCostEvent extends Event {

    public final int card;
    public final int cost;

    public PayCostEvent(int card, int cost) {
        this.card = card;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "PayCostEvent{" + "card=" + card + ", card=" + card + '}';
    }
}
