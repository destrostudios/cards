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
import com.destrostudios.cards.frontend.cardgui.targetarrow.TargetSnapMode;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.jme3.math.Vector3f;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private Map<Integer, List<BattleEvent>> possibleBattleEventsPerSource = new HashMap<>();

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

    // TODO: Cleanup / Extract
    public void updateInteractivities(List<Event> possibleEvents) {
        EntityData entityData = gameClient.getGame().getData();
        possibleBattleEventsPerSource.clear();
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
            else if (event instanceof BattleEvent) {
                BattleEvent battleEvent = (BattleEvent) event;
                List<BattleEvent> battleEventsOfSource = possibleBattleEventsPerSource.computeIfAbsent(battleEvent.source, (entity) -> new LinkedList<>());
                battleEventsOfSource.add(battleEvent);
            }
        }
        for (Map.Entry<Integer, List<BattleEvent>> battleEventsEntry : possibleBattleEventsPerSource.entrySet()) {
            int attackingEntity = battleEventsEntry.getKey();
            List<BattleEvent> battleEventsOfSource = battleEventsEntry.getValue();
            Card<CardModel> attackingCard = cardGuiMap.getOrCreateCard(attackingEntity);
            attackingCard.setInteractivity(new AimToTargetInteractivity<CardModel>(TargetSnapMode.VALID) {

                @Override
                public boolean isValid(BoardObject boardObject) {
                    return (getBattleEvent(boardObject) != null);
                }

                @Override
                public void trigger(BoardObject<CardModel> boardObject, BoardObject target) {
                    gameClient.requestAction(getBattleEvent(target));
                }

                private Event getBattleEvent(BoardObject targetBoardObject) {
                    if (targetBoardObject instanceof Card) {
                        Card<CardModel> targetCard = (Card) targetBoardObject;
                        int targetEntity = cardGuiMap.getEntity(targetCard);
                        for (BattleEvent battleEvent : battleEventsOfSource) {
                            if (battleEvent.target == targetEntity) {
                                return battleEvent;
                            }
                        }
                    }
                    return null;
                }
            });
        }
    }
}
