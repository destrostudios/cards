package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.rules.Components;
import lombok.Getter;

import java.util.HashMap;

public class EntityService {

    public EntityService(CardService cardService) {
        this.cardService = cardService;
        cardEntities = new HashMap<>();
        loadEntities();
    }
    private CardService cardService;
    @Getter
    private EntityData data;
    private HashMap<Integer, Integer> cardEntities;

    private void loadEntities() {
        data = new SimpleEntityData(Components.ALL);
        for (Card card : cardService.getCards()) {
            int cardEntity = data.createEntity();
            EntityTemplate.loadTemplate(data, cardEntity, card.getPath());
            cardEntities.put(card.getId(), cardEntity);
        }
    }

    public boolean hasComponent(int cardId, ComponentDefinition<?> component) {
        return data.hasComponent(getEntity(cardId), component);
    }

    private int getEntity(int cardId) {
        return cardEntities.get(cardId);
    }
}
