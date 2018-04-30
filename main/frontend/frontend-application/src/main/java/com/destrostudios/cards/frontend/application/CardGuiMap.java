package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;

import java.util.HashMap;

public class CardGuiMap {

    private HashMap<Integer, Card<CardModel>> visualCards = new HashMap<>();
    private HashMap<Card<CardModel>, Integer> gameEntities = new HashMap<>();

    public Card<CardModel> getOrCreateCard(int cardEntity) {
        Card<CardModel> card = visualCards.get(cardEntity);
        if (card == null) {
            card = new Card<>(new CardModel());
            visualCards.put(cardEntity, card);
            gameEntities.put(card, cardEntity);
        }
        return card;
    }
}
