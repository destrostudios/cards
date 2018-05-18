package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.application.PlayerZones;
import com.destrostudios.cards.frontend.application.SimpleGameClient;
import com.destrostudios.cards.frontend.cardgui.Board;
import com.destrostudios.cards.frontend.cardgui.BoardObject;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardgui.CardZone;
import com.destrostudios.cards.frontend.cardgui.events.*;
import com.destrostudios.cards.frontend.cardgui.interactivities.*;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.PlayCardFromHandEvent;
import com.jme3.math.Vector3f;

import java.util.HashMap;
import java.util.List;

public class UpdateBoardService {

    public UpdateBoardService(SimpleGameClient gameClient, Board<CardModel> board, HashMap<Integer, PlayerZones> playerZonesMap, CardGuiMap cardGuiMap) {
        this.gameClient = gameClient;
        this.board = board;
        this.playerZonesMap = playerZonesMap;
        this.cardGuiMap = cardGuiMap;
    }
    private SimpleGameClient gameClient;
    private Board<CardModel> board;
    private HashMap<Integer, PlayerZones> playerZonesMap;
    private CardGuiMap cardGuiMap;

    public void updateAndResetInteractivities() {
        EntityData entityData = gameClient.getGame().getData();
        IntArrayList cardEntities = entityData.entities(Components.OWNED_BY);
        for (int cardEntity : cardEntities) {
            CardZone cardZone = null;
            Integer cardZoneIndex;

            int playerEntity = entityData.getComponent(cardEntity, Components.OWNED_BY);
            PlayerZones playerZones = playerZonesMap.get(playerEntity);

            cardZoneIndex = entityData.getComponent(cardEntity, Components.LIBRARY);
            if (cardZoneIndex != null) {
                cardZone = playerZones.getDeckZone();
            }
            else {
                cardZoneIndex = entityData.getComponent(cardEntity, Components.HAND_CARDS);
                if (cardZoneIndex != null) {
                    cardZone = playerZones.getHandZone();
                }
                else {
                    cardZoneIndex = entityData.getComponent(cardEntity, Components.CREATURE_ZONE);
                    if (cardZoneIndex != null) {
                        cardZone = playerZones.getBoardZone();
                    }
                }
            }

            Card<CardModel> card = cardGuiMap.getOrCreateCard(cardEntity);
            card.setInteractivity(null);

            if (cardZoneIndex != null) {
                CardGuiMapper.updateModel(card, entityData, cardEntity);
                board.triggerEvent(new ModelUpdatedEvent(card));
                board.triggerEvent(new MoveCardEvent(card, cardZone, new Vector3f(cardZoneIndex, 0, 0)));
            }
        }
    }

    public void updateInteractivities(List<Event> possibleEvents) {
        EntityData entityData = gameClient.getGame().getData();
        // TODO: Cleanup / Extract
        for (Event event : possibleEvents) {
            if (event instanceof DrawCardEvent) {
                DrawCardEvent drawCardEvent = (DrawCardEvent) event;
                for (int ownLibraryCardEntity : entityData.entities(Components.LIBRARY, cardEntity -> entityData.hasComponentValue(cardEntity, Components.OWNED_BY, drawCardEvent.player))) {
                    Card<CardModel> libraryCard = cardGuiMap.getOrCreateCard(ownLibraryCardEntity);
                    libraryCard.setInteractivity(new ClickInteractivity<CardModel>() {

                        @Override
                        public void trigger(BoardObject<CardModel> boardObject, BoardObject target) {
                            gameClient.requestAction(new DrawCardEvent(drawCardEvent.player));
                        }
                    });
                }
            }
            else if (event instanceof PlayCardFromHandEvent) {
                PlayCardFromHandEvent playCardFromHandEvent = (PlayCardFromHandEvent) event;
                Card<CardModel> handCard = cardGuiMap.getOrCreateCard(playCardFromHandEvent.card);
                handCard.setInteractivity(new DragToPlayInteractivity<CardModel>() {
                    @Override
                    public void trigger(BoardObject<CardModel> boardObject, BoardObject target) {
                        gameClient.requestAction(new PlayCardFromHandEvent(playCardFromHandEvent.card));
                    }
                });
            }
            else if (event instanceof DamageEvent) {
                DamageEvent damageEvent = (DamageEvent) event;
                Card<CardModel> cardToDamage = cardGuiMap.getOrCreateCard(damageEvent.target);
                cardToDamage.setInteractivity(new ClickInteractivity<CardModel>() {

                    @Override
                    public void trigger(BoardObject<CardModel> boardObject, BoardObject target) {
                        gameClient.requestAction(new DamageEvent(damageEvent.target, 1));
                    }
                });
            }
        }
    }
}
