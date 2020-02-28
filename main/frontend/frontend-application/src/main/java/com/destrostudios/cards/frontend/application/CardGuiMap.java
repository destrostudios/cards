package com.destrostudios.cards.frontend.application;

import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.JMonkeyUtil;
import com.destrostudios.cardgui.transformations.SimpleTargetRotationTransformation;
import com.destrostudios.cardgui.samples.transformations.relative.HoveringTransformation;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.HashMap;

public class CardGuiMap {

    public CardGuiMap(EntityData entityData) {
        this.entityData = entityData;
    }
    private EntityData entityData;
    private HashMap<Integer, Card<CardModel>> visualCards = new HashMap<>();
    private HashMap<Card<CardModel>, Integer> gameEntities = new HashMap<>();

    public Card<CardModel> getOrCreateCard(int cardEntity) {
        Card<CardModel> card = visualCards.get(cardEntity);
        if (card == null) {
            card = new Card<>(new CardModel());
            initialConfiguration(cardEntity, card);
            visualCards.put(cardEntity, card);
            gameEntities.put(card, cardEntity);
        }
        return card;
    }

    public int getEntity(Card<CardModel> card) {
        return gameEntities.get(card);
    }

    // TODO: Extract to better place. Pass as initializer interface?
    private void initialConfiguration(int cardEntity, Card<CardModel> card) {
        card.position().addRelativeTransformation(new HoveringTransformation(0.3f, 2), () -> entityData.hasComponent(cardEntity, Components.Ability.FLYING) && entityData.hasComponent(cardEntity, Components.BOARD));
        card.rotation().addRelativeTransformation(new SimpleTargetRotationTransformation(JMonkeyUtil.getQuaternion_Y(-90)), () -> entityData.hasComponent(cardEntity, Components.TAPPED));
    }
}
