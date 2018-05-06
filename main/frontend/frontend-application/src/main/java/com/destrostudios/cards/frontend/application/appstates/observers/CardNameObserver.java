package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.Components;

public class CardNameObserver extends CardComponentObserver<String> {

    public CardNameObserver(CardGuiMap cardGuiMap) {
        super(cardGuiMap, Components.DISPLAY_NAME);
    }

    @Override
    protected void onSet(Card<CardModel> card, String value) {
        card.getModel().setTitle(value);
    }

    @Override
    protected void onRemoved(Card<CardModel> card) {
        card.getModel().setTitle(null);
    }
}
