package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.util.MixedManaAmount;

public class PayCostEvent extends Event {

    public final int card;
    public final int cost;
    public final MixedManaAmount payedMana;

    public PayCostEvent(int card, int cost, MixedManaAmount payedMana) {
        this.card = card;
        this.cost = cost;
        this.payedMana = payedMana;
    }

    @Override
    public String toString() {
        return "PayCostEvent{" + "card=" + card + ", card=" + card + ", payedMana=" + payedMana + '}';
    }
}
