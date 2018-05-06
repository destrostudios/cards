package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.ComponentDefinition;

public abstract class CardComponentObserver<T> extends GuiComponentObserver<T> {

    public CardComponentObserver(CardGuiMap cardGuiMap, ComponentDefinition<T> component) {
        super(component);
        this.cardGuiMap = cardGuiMap;
    }
    private CardGuiMap cardGuiMap;

    @Override
    public void onValueAdded(int entity, T value) {
        onSet(getCardModel(entity), value);
    }

    @Override
    public void onValueChanged(int entity, T newValue, T oldValue) {
        onSet(getCardModel(entity), newValue);
    }

    @Override
    public void onValueRemoved(int entity, T value) {
        onRemoved(getCardModel(entity));
    }

    protected abstract void onSet(Card<CardModel> card, T value);

    protected abstract void onRemoved(Card<CardModel> card);

    private Card<CardModel> getCardModel(int cardEntity) {
        return cardGuiMap.getOrCreateCard(cardEntity);
    }
}
