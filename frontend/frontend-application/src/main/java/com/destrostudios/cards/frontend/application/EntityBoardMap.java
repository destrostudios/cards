package com.destrostudios.cards.frontend.application;

import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.TransformedBoardObject;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.players.PlayerBoardObject;
import com.destrostudios.cards.shared.entities.EntityData;

import java.util.HashMap;
import java.util.function.Supplier;

public class EntityBoardMap {

    public EntityBoardMap(EntityData entityData) {
        this.entityData = entityData;
    }
    private EntityData entityData;
    private HashMap<Integer, TransformedBoardObject<?>> boardObjects = new HashMap<>();
    private HashMap<TransformedBoardObject<?>, Integer> entities = new HashMap<>();

    public Card<CardModel> getOrCreateCard(int cardEntity) {
        return getOrCreateBoardObject(cardEntity, () -> {
            Card<CardModel> card = new Card<>(new CardModel());
            initialCardConfiguration(cardEntity, card);
            return card;
        });
    }

    // TODO: Extract to better place. Pass as initializer interface?
    private void initialCardConfiguration(int cardEntity, Card<CardModel> card) {
        // card.position().addRelativeTransformation(new HoveringTransformation(0.3f, 2), () -> entityData.hasComponent(cardEntity, Components.Ability.FLYING) && entityData.hasComponent(cardEntity, Components.BOARD));
        // card.rotation().addRelativeTransformation(new SimpleTargetRotationTransformation(JMonkeyUtil.getQuaternion_Y(-90)), () -> entityData.hasComponent(cardEntity, Components.TAPPED));
    }

    public PlayerBoardObject getOrCreatePlayer(int playerEntity) {
        return getOrCreateBoardObject(playerEntity, PlayerBoardObject::new);
    }

    private <T extends TransformedBoardObject<?>> T getOrCreateBoardObject(int entity, Supplier<T> supplier) {
        T boardObject = (T) boardObjects.get(entity);
        if (boardObject == null) {
            boardObject = supplier.get();
            boardObjects.put(entity, boardObject);
            entities.put(boardObject, entity);
        }
        return boardObject;
    }

    public TransformedBoardObject<?> getBoardObject(int entity) {
        return boardObjects.get(entity);
    }

    public Integer getEntity(TransformedBoardObject<?> boardObject) {
        return entities.get(boardObject);
    }
}
