package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.cardpainter.model.Color;
import com.destrostudios.cards.shared.entities.ComponentDefinition;

public class CardColorObserver extends CardComponentObserver<Void> {

    public CardColorObserver(CardGuiMap cardGuiMap, ComponentDefinition<Void> colorComponent, Color color) {
        super(cardGuiMap, colorComponent);
        this.color = color;
    }
    private Color color;

    @Override
    protected void onSet(Card<CardModel> card, Void value) {
        card.getModel().addColor(color);
    }

    @Override
    protected void onRemoved(Card<CardModel> card) {
        card.getModel().removeColor(color);
    }
}
