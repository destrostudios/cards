package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.events.*;
import com.destrostudios.cardgui.interactivities.*;
import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.application.PlayerZones;
import com.destrostudios.cards.frontend.application.SimpleGameClient;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TargetRuleValidator;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.jme3.math.Vector3f;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class UpdateBoardService {

    public UpdateBoardService(SimpleGameClient gameClient, Board board, HashMap<Integer, PlayerZones> playerZonesMap, CardGuiMap cardGuiMap) {
        this.gameClient = gameClient;
        this.board = board;
        this.playerZonesMap = playerZonesMap;
        this.cardGuiMap = cardGuiMap;
    }
    private SimpleGameClient gameClient;
    private Board board;
    private HashMap<Integer, PlayerZones> playerZonesMap;
    private CardGuiMap cardGuiMap;
    private Map<Integer, List<Event>> possibleDeclareBattleEventsPerSource = new HashMap<>();

    public void updateAndResetInteractivities() {
        EntityData entityData = gameClient.getGame().getData();
        List<Integer> cardEntities = entityData.query(Components.OWNED_BY).list();
        for (int cardEntity : cardEntities) {
            CardZone cardZone = null;
            Integer cardZoneIndex;

            int playerEntity = entityData.getComponent(cardEntity, Components.OWNED_BY);
            PlayerZones playerZones = playerZonesMap.get(playerEntity);

            // TODO: Yeah...
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
                    cardZoneIndex = entityData.getComponent(cardEntity, Components.LAND_ZONE);
                    if (cardZoneIndex != null) {
                        cardZone = playerZones.getLandZone();
                    }
                    else {
                        cardZoneIndex = entityData.getComponent(cardEntity, Components.CREATURE_ZONE);
                        if (cardZoneIndex != null) {
                            cardZone = playerZones.getCreatureZone();
                        }
                        else {
                            cardZoneIndex = entityData.getComponent(cardEntity, Components.ENCHANTMENT_ZONE);
                            if (cardZoneIndex != null) {
                                cardZone = playerZones.getEnchantmentZone();
                            }
                            else {
                                cardZoneIndex = entityData.getComponent(cardEntity, Components.GRAVEYARD);
                                if (cardZoneIndex != null) {
                                    cardZone = playerZones.getGraveyardZone();
                                }
                            }
                        }
                    }
                }
            }

            Card<CardModel> card = cardGuiMap.getOrCreateCard(cardEntity);
            card.setInteractivity(null);

            if (cardZoneIndex != null) {
                CardGuiMapper.updateModel(card, entityData, cardEntity);
                board.triggerEvent(new MoveCardEvent(card, cardZone, new Vector3f(cardZoneIndex, 0, 0)));
            }
        }
    }

    // TODO: Cleanup / Extract
    public void updateInteractivities(List<Event> possibleEvents) {
        EntityData entityData = gameClient.getGame().getData();
        possibleDeclareBattleEventsPerSource.clear();
        for (Event event : possibleEvents) {
            if (event instanceof DrawCardEvent) {
                DrawCardEvent drawCardEvent = (DrawCardEvent) event;
                for (int ownLibraryCardEntity : entityData.query(Components.LIBRARY).list(cardEntity -> entityData.hasComponentValue(cardEntity, Components.OWNED_BY, drawCardEvent.player))) {
                    Card<CardModel> libraryCard = cardGuiMap.getOrCreateCard(ownLibraryCardEntity);
                    libraryCard.setInteractivity(new ClickInteractivity() {

                        @Override
                        public void trigger(BoardObject source, BoardObject target) {
                            gameClient.requestAction(drawCardEvent);
                        }
                    });
                }
            }
            else if (event instanceof PlaySpellEvent) {
                PlaySpellEvent playSpellEvent = (PlaySpellEvent) event;
                // TODO: Improve?
                int cardEntity = entityData.query(Components.SPELL_ENTITIES)
                        .unique(currentCardEntity -> IntStream.of(entityData.getComponent(currentCardEntity, Components.SPELL_ENTITIES))
                        .anyMatch(entity -> entity == playSpellEvent.spell)).getAsInt();
                Card<CardModel> card = cardGuiMap.getOrCreateCard(cardEntity);

                Interactivity interactivity;
                Integer targetRuleEntity = entityData.getComponent(playSpellEvent.spell, Components.Spell.TARGET_RULE);
                if (targetRuleEntity != null) {
                    interactivity = new AimToTargetInteractivity(TargetSnapMode.VALID) {

                        @Override
                        public boolean isValid(BoardObject boardObject) {
                            if (boardObject instanceof Card) {
                                int targetEntity = cardGuiMap.getEntity((Card) boardObject);
                                return TargetRuleValidator.isValidTarget(entityData, targetRuleEntity, cardEntity, targetEntity);
                            }
                            return false;
                        }

                        @Override
                        public void trigger(BoardObject boardObject, BoardObject target) {
                            int targetEntity = cardGuiMap.getEntity((Card) target);
                            // TODO:
                            int[] targets = new int[]{targetEntity};
                            gameClient.requestAction(new PlaySpellEvent(playSpellEvent.spell, targets));
                        }
                    };
                }
                else if (entityData.hasComponent(cardEntity, Components.HAND_CARDS)) {
                    interactivity = new DragToPlayInteractivity() {

                        @Override
                        public void trigger(BoardObject boardObject, BoardObject target) {
                            gameClient.requestAction(playSpellEvent);
                        }
                    };
                }
                else {
                    interactivity = new ClickInteractivity() {

                        @Override
                        public void trigger(BoardObject boardObject, BoardObject target) {
                            gameClient.requestAction(playSpellEvent);
                        }
                    };
                }
                card.setInteractivity(interactivity);
                card.getModel().setPlayable(true);
            }
            else if (event instanceof DeclareAttackEvent) {
                DeclareAttackEvent declareAttackEvent = (DeclareAttackEvent) event;
                addPossibleDeclareBattleEvent(declareAttackEvent.source, event);
            }
            else if (event instanceof DeclareBlockEvent) {
                DeclareBlockEvent declareBlockEvent = (DeclareBlockEvent) event;
                addPossibleDeclareBattleEvent(declareBlockEvent.source, event);
            }
        }
        for (Map.Entry<Integer, List<Event>> declareBattleEventsEntry : possibleDeclareBattleEventsPerSource.entrySet()) {
            int declaringEntity = declareBattleEventsEntry.getKey();
            List<Event> declareBattleEventsOfSource = declareBattleEventsEntry.getValue();
            Card<CardModel> declaringCard = cardGuiMap.getOrCreateCard(declaringEntity);
            declaringCard.setInteractivity(new AimToTargetInteractivity(TargetSnapMode.VALID) {

                @Override
                public boolean isValid(BoardObject boardObject) {
                    return (getDeclareBattleEvent(boardObject) != null);
                }

                @Override
                public void trigger(BoardObject source, BoardObject target) {
                    gameClient.requestAction(getDeclareBattleEvent(target));
                }

                private Event getDeclareBattleEvent(BoardObject targetBoardObject) {
                    Integer targetEntity = null;
                    if (targetBoardObject instanceof Card) {
                        Card<CardModel> targetCard = (Card) targetBoardObject;
                        targetEntity = cardGuiMap.getEntity(targetCard);
                    }
                    else if (targetBoardObject instanceof CardZone) {
                        CardZone targetCardZone = (CardZone) targetBoardObject;
                        for (Map.Entry<Integer, PlayerZones> playerZonesEntry : playerZonesMap.entrySet()) {
                            int playerEntity = playerZonesEntry.getKey();
                            PlayerZones playerZones = playerZonesEntry.getValue();
                            for (CardZone cardZone : playerZones.getZones()) {
                                if (cardZone == targetCardZone) {
                                    targetEntity = playerEntity;
                                    break;
                                }
                            }
                            if (targetEntity != null) {
                                break;
                            }
                        }
                    }
                    if (targetEntity != null) {
                        for (Event declareBattleEvent : declareBattleEventsOfSource) {
                            boolean isDeclaredTarget = false;
                            if (declareBattleEvent instanceof DeclareAttackEvent) {
                                DeclareAttackEvent declareAttackEvent = (DeclareAttackEvent) declareBattleEvent;
                                isDeclaredTarget = (declareAttackEvent.target == targetEntity);
                            } else if (declareBattleEvent instanceof DeclareBlockEvent) {
                                DeclareBlockEvent declareBlockEvent = (DeclareBlockEvent) declareBattleEvent;
                                isDeclaredTarget = (declareBlockEvent.target == targetEntity);
                            }
                            if (isDeclaredTarget) {
                                return declareBattleEvent;
                            }
                        }
                    }
                    return null;
                }
            });
            declaringCard.getModel().setPlayable(true);
        }
    }

    private void addPossibleDeclareBattleEvent(int declaringEntity, Event event) {
        List<Event> declareBattleEventsOfSource = possibleDeclareBattleEventsPerSource.computeIfAbsent(declaringEntity, (entity) -> new LinkedList<>());
        declareBattleEventsOfSource.add(event);
    }
}
