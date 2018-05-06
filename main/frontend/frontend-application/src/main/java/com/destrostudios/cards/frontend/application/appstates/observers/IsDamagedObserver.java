package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.Components;

public class IsDamagedObserver extends CardComponentObserver<Void> {

    public IsDamagedObserver(CardGuiMap cardGuiMap) {
        super(cardGuiMap, Components.DAMAGED);
    }

    @Override
    protected void onSet(Card<CardModel> card, Void value) {
        card.getModel().setDamaged(true);
    }

    @Override
    protected void onRemoved(Card<CardModel> card) {
        card.getModel().setDamaged(false);
    }
}
