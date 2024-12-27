package com.destrostudios.cards.frontend.application;

import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.TransformedBoardObject;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.players.PlayerBoardObject;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.HashMap;

public class EntityBoardMap {

    public EntityBoardMap(EntityData entityData) {
        this.entityData = entityData;
    }
    private EntityData entityData;
    private HashMap<Integer, TransformedBoardObject<?>> boardObjects = new HashMap<>();
    private HashMap<TransformedBoardObject<?>, Integer> entities = new HashMap<>();

    public PlayerBoardObject getOrCreatePlayer(int entity) {
        return (PlayerBoardObject) getOrCreateBoardObject(entity);
    }

    public Card<CardModel> getOrCreateCard(int entity) {
        return (Card<CardModel>) getOrCreateBoardObject(entity);
    }

    public TransformedBoardObject<?> getOrCreateBoardObject(int entity) {
        TransformedBoardObject boardObject = boardObjects.get(entity);
        if (boardObject == null) {
            if (entityData.hasComponent(entity, Components.NEXT_PLAYER)) {
                boardObject = new PlayerBoardObject();
            } else {
                Card<CardModel> card = new Card<>(new CardModel());
                initialCardConfiguration(entity, card);
                boardObject = card;
            }
            boardObjects.put(entity, boardObject);
            entities.put(boardObject, entity);
        }
        return boardObject;
    }

    // TODO: Extract to better place. Pass as initializer interface?
    private void initialCardConfiguration(int cardEntity, Card<CardModel> card) {
        // card.position().addRelativeTransformation(new HoveringTransformation(0.3f, 2), () -> entityData.hasComponent(cardEntity, Components.Ability.FLYING) && entityData.hasComponent(cardEntity, Components.BOARD));
        // card.rotation().addRelativeTransformation(new SimpleTargetRotationTransformation(JMonkeyUtil.getQuaternion_Y(-90)), () -> entityData.hasComponent(cardEntity, Components.TAPPED));
    }

    public Integer getEntity(TransformedBoardObject<?> boardObject) {
        return entities.get(boardObject);
    }
}
