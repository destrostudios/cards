package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderAppState;
import com.destrostudios.cards.frontend.application.appstates.services.CardGuiMapper;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.model.CardIdentifier;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.CardListCard;
import com.destrostudios.cards.shared.rules.Components;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CachedModelsDeckAppState<DBAS extends DeckBuilderAppState<CardModel>> extends DeckAppState<DBAS> {

    private HashMap<String, CardModel> cardsToModels = new HashMap<>();
    private HashMap<CardModel, CardIdentifier> modelsToCards = new HashMap<>();

    protected List<CardModel> mapCards(List<CardIdentifier> cardIdentifiers) {
        mapAndStoreCardList(cardIdentifiers);
        return cardIdentifiers.stream().map(this::getCardModel).collect(Collectors.toList());
    }

    protected HashMap<CardModel, Integer> mapCardList(CardList cardList) {
        mapAndStoreCardList(cardList.getCards());
        HashMap<CardModel, Integer> cardModels = new HashMap<>();
        for (CardListCard cardListCard : cardList.getCards()) {
            CardModel cardModel = getCardModel(cardListCard);
            cardModels.put(cardModel, cardListCard.getAmount());
        }
        return cardModels;
    }

    private void mapAndStoreCardList(List<? extends CardIdentifier> cardIdentifiers) {
        EntityData data = new SimpleEntityData(Components.ALL);
        for (CardIdentifier cardIdentifier : cardIdentifiers) {
            String cardKey = getCardKey(cardIdentifier);
            cardsToModels.computeIfAbsent(cardKey, ck -> {
                CardModel cardModel = CardGuiMapper.createModel(data, cardIdentifier);
                modelsToCards.put(cardModel, cardIdentifier);
                return cardModel;
            });
        }
    }

    private CardModel getCardModel(CardIdentifier cardIdentifier) {
        return cardsToModels.get(getCardKey(cardIdentifier));
    }

    private String getCardKey(CardIdentifier cardIdentifier) {
        return cardIdentifier.getCard().getId() + "_" + cardIdentifier.getFoil().getId();
    }

    protected CardIdentifier getCardIdentifier(CardModel cardModel) {
        return modelsToCards.get(cardModel);
    }
}
