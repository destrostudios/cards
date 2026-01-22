package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderAppState;
import com.destrostudios.cards.frontend.application.appstates.services.CardGuiMapper;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.model.CardIdentifier;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.CardListCard;
import com.destrostudios.cards.shared.model.Power;
import com.destrostudios.cards.shared.rules.Components;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CachedModelsDeckAppState<DBAS extends DeckBuilderAppState<CardModel>> extends DeckAppState<DBAS> {

    private HashMap<String, CardModel> keysToModels = new HashMap<>();
    private HashMap<CardModel, CardIdentifier> modelsToCards = new HashMap<>();
    private HashMap<CardModel, Power> modelsToPowers = new HashMap<>();

    protected List<CardModel> mapCards(List<? extends CardIdentifier> cardIdentifiers) {
        mapAndStoreCardList(cardIdentifiers);
        return cardIdentifiers.stream().map(this::getCardModel).collect(Collectors.toList());
    }

    protected List<CardModel> mapPowers(List<Power> powers) {
        mapAndStorePowers(powers);
        return powers.stream().map(this::getPowerModel).collect(Collectors.toList());
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

    protected void putOneOfEachCard(HashMap<CardModel, Integer> cardAmounts, List<CardModel> cardModels) {
        for (CardModel cardModel : cardModels) {
            cardAmounts.put(cardModel, 1);
        }
    }

    private void mapAndStoreCardList(List<? extends CardIdentifier> cardIdentifiers) {
        EntityData data = new SimpleEntityData(Components.ALL);
        for (CardIdentifier cardIdentifier : cardIdentifiers) {
            String cardKey = getCardKey(cardIdentifier);
            keysToModels.computeIfAbsent(cardKey, _ -> {
                CardModel cardModel = CardGuiMapper.createModel(data, cardIdentifier);
                modelsToCards.put(cardModel, cardIdentifier);
                return cardModel;
            });
        }
    }

    private void mapAndStorePowers(List<Power> powers) {
        EntityData data = new SimpleEntityData(Components.ALL);
        for (Power power : powers) {
            String powerKey = getPowerKey(power);
            keysToModels.computeIfAbsent(powerKey, _ -> {
                CardModel cardModel = CardGuiMapper.createModel(data, power.getPath(), null);
                modelsToPowers.put(cardModel, power);
                return cardModel;
            });
        }
    }

    private CardModel getCardModel(CardIdentifier cardIdentifier) {
        return keysToModels.get(getCardKey(cardIdentifier));
    }

    protected CardModel getPowerModel(Power power) {
        return keysToModels.get(getPowerKey(power));
    }

    private String getCardKey(CardIdentifier cardIdentifier) {
        return "card_" + cardIdentifier.getCard().getId() + "_" + cardIdentifier.getFoil().getId();
    }

    private String getPowerKey(Power power) {
        return "power_" + power.getId();
    }

    protected CardIdentifier getCardIdentifier(CardModel cardModel) {
        return modelsToCards.get(cardModel);
    }

    protected Power getPower(CardModel cardModel) {
        return modelsToPowers.get(cardModel);
    }
}
