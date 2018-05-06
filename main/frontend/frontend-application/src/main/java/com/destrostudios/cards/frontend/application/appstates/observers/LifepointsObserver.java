package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.Components;

public class LifepointsObserver extends CardComponentObserver<Integer> {

    public LifepointsObserver(CardGuiMap cardGuiMap) {
        super(cardGuiMap, Components.HEALTH);
    }

    @Override
    protected void onSet(Card<CardModel> card, Integer value) {
        card.getModel().setLifepoints(value);
    }

    @Override
    protected void onRemoved(Card<CardModel> card) {
        card.getModel().setLifepoints(null);
    }
}
