package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.ComponentDefinition;

public class AbilityObserver extends CardComponentObserver<Void> {

    public AbilityObserver(CardGuiMap cardGuiMap, ComponentDefinition<Void> abilityComponent, String abilityTitle) {
        super(cardGuiMap, abilityComponent);
        this.abilityTitle = abilityTitle;
    }
    private String abilityTitle;

    @Override
    protected void onSet(Card<CardModel> card, Void value) {
        card.getModel().addKeyword(abilityTitle);
    }

    @Override
    protected void onRemoved(Card<CardModel> card) {
        card.getModel().removeKeyword(abilityTitle);
    }
}
