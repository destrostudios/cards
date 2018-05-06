package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.ComponentDefinition;

public class TribeObserver extends CardComponentObserver<Void> {

    public TribeObserver(CardGuiMap cardGuiMap, ComponentDefinition<Void> tribeComponent, String tribeTitle) {
        super(cardGuiMap, tribeComponent);
        this.tribeTitle = tribeTitle;
    }
    private String tribeTitle;

    @Override
    protected void onSet(Card<CardModel> card, Void value) {
        card.getModel().addTribe(tribeTitle);
    }

    @Override
    protected void onRemoved(Card<CardModel> card) {
        card.getModel().removeTribe(tribeTitle);
    }
}
