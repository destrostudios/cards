package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.Components;

public class AttackDamageObserver extends CardComponentObserver<Integer> {

    public AttackDamageObserver(CardGuiMap cardGuiMap) {
        super(cardGuiMap, Components.ATTACK);
    }

    @Override
    protected void onSet(Card<CardModel> card, Integer value) {
        card.getModel().setAttackDamage(value);
    }

    @Override
    protected void onRemoved(Card<CardModel> card) {
        card.getModel().setAttackDamage(null);
    }
}
