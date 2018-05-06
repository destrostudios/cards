package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.Components;

public class FlavourTextObserver extends CardComponentObserver<String> {

    public FlavourTextObserver(CardGuiMap cardGuiMap) {
        super(cardGuiMap, Components.FLAVOUR_TEXT);
    }

    @Override
    protected void onSet(Card<CardModel> card, String value) {
        card.getModel().setFlavourText(value);
    }

    @Override
    protected void onRemoved(Card<CardModel> card) {
        card.getModel().setFlavourText(null);
    }
}
