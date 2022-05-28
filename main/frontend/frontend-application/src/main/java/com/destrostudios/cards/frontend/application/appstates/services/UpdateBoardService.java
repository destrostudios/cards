package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.events.*;
import com.destrostudios.cardgui.interactivities.*;
import com.destrostudios.cards.frontend.application.CardGuiMap;
import com.destrostudios.cards.frontend.application.PlayerZones;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.jme3.math.Vector3f;

import java.util.*;
import java.util.stream.IntStream;

public class UpdateBoardService {

    public UpdateBoardService(GameService gameService, Board board, HashMap<Integer, PlayerZones> playerZonesMap, CardGuiMap cardGuiMap) {
        this.gameService = gameService;
        this.board = board;
        this.playerZonesMap = playerZonesMap;
        this.cardGuiMap = cardGuiMap;
    }
    private GameService gameService;
    private Board board;
    private HashMap<Integer, PlayerZones> playerZonesMap;
    private CardGuiMap cardGuiMap;
    private Map<Integer, List<AttackEvent>> possibleAttackEventsPerSource = new HashMap<>();

    public void update(List<Event> possibleEvents) {
        EntityData data = gameService.getGameContext().getData();
        List<Integer> cardEntities = data.query(Components.OWNED_BY).list();
        for (int cardEntity : cardEntities) {
            CardZone cardZone = null;
            Integer cardZoneIndex;

            int playerEntity = data.getComponent(cardEntity, Components.OWNED_BY);
            PlayerZones playerZones = playerZonesMap.get(playerEntity);

            // TODO: Yeah...
            cardZoneIndex = data.getComponent(cardEntity, Components.LIBRARY);
            if (cardZoneIndex != null) {
                cardZone = playerZones.getDeckZone();
            }
            else {
                cardZoneIndex = data.getComponent(cardEntity, Components.HAND);
                if (cardZoneIndex != null) {
                    cardZone = playerZones.getHandZone();
                }
                else {
                    cardZoneIndex = data.getComponent(cardEntity, Components.SPELL_ZONE);
                    if (cardZoneIndex != null) {
                        cardZone = playerZones.getSpellZone();
                    }
                    else {
                        cardZoneIndex = data.getComponent(cardEntity, Components.CREATURE_ZONE);
                        if (cardZoneIndex != null) {
                            cardZone = playerZones.getCreatureZone();
                        }
                        else {
                            cardZoneIndex = data.getComponent(cardEntity, Components.GRAVEYARD);
                            if (cardZoneIndex != null) {
                                cardZone = playerZones.getGraveyardZone();
                            }
                        }
                    }
                }
            }

            Card<CardModel> card = cardGuiMap.getOrCreateCard(cardEntity);
            card.clearInteractivities();

            if (cardZoneIndex != null) {
                CardGuiMapper.updateModel(card, data, cardEntity);
                board.triggerEvent(new MoveCardEvent(card, cardZone, new Vector3f(cardZoneIndex, 0, 0)));
            }
        }

        if (possibleEvents != null) {
            updateInteractivities(possibleEvents);
        }
    }

    private void updateInteractivities(List<Event> possibleEvents) {
        EntityData data = gameService.getGameContext().getData();
        possibleAttackEventsPerSource.clear();
        for (Event event : possibleEvents) {
            if (event instanceof PlaySpellEvent) {
                PlaySpellEvent playSpellEvent = (PlaySpellEvent) event;
                // TODO: Improve?
                int cardEntity = data.query(Components.SPELL_ENTITIES)
                        .unique(currentCardEntity -> IntStream.of(data.getComponent(currentCardEntity, Components.SPELL_ENTITIES))
                        .anyMatch(entity -> entity == playSpellEvent.spell)).getAsInt();
                Card<CardModel> card = cardGuiMap.getOrCreateCard(cardEntity);

                Interactivity interactivity;
                if (SpellUtil.isTargeted(data, playSpellEvent.spell) && (playSpellEvent.targets.length > 0)) {
                    interactivity = new AimToTargetInteractivity(TargetSnapMode.VALID) {

                        @Override
                        public boolean isValid(BoardObject boardObject) {
                            if (boardObject instanceof Card) {
                                int targetEntity = cardGuiMap.getEntity((Card) boardObject);
                                return SpellUtil.isCastable(data, cardEntity, playSpellEvent.spell, new int[] { targetEntity });
                            }
                            return false;
                        }

                        @Override
                        public void trigger(BoardObject boardObject, BoardObject target) {
                            int targetEntity = cardGuiMap.getEntity((Card) target);
                            gameService.sendAction(new PlaySpellEvent(playSpellEvent.spell, new int[]{ targetEntity }));
                        }
                    };
                } else if (data.hasComponent(cardEntity, Components.HAND)) {
                    interactivity = new DragToPlayInteractivity() {

                        @Override
                        public void trigger(BoardObject boardObject, BoardObject target) {
                            gameService.sendAction(playSpellEvent);
                        }
                    };
                } else {
                    interactivity = new ClickInteractivity() {

                        @Override
                        public void trigger(BoardObject boardObject, BoardObject target) {
                            gameService.sendAction(playSpellEvent);
                        }
                    };
                }
                InteractivitySource interactivitySource = (CardGuiMapper.isDefaultCastFromHandSpell(data, playSpellEvent.spell) ? InteractivitySource.MOUSE_LEFT : InteractivitySource.MOUSE_RIGHT);
                card.setInteractivity(interactivitySource, interactivity);
                card.getModel().setPlayable(true);
            }
            else if (event instanceof AttackEvent) {
                AttackEvent attackEvent = (AttackEvent) event;
                List<AttackEvent> attackEventsOfSource = possibleAttackEventsPerSource.computeIfAbsent(attackEvent.source, (entity) -> new LinkedList<>());
                attackEventsOfSource.add(attackEvent);
            }
        }
        for (Map.Entry<Integer, List<AttackEvent>> attackEventsEntry : possibleAttackEventsPerSource.entrySet()) {
            int attackingEntity = attackEventsEntry.getKey();
            List<AttackEvent> attackEventsOfSource = attackEventsEntry.getValue();
            Card<CardModel> attackingCard = cardGuiMap.getOrCreateCard(attackingEntity);
            attackingCard.setInteractivity(InteractivitySource.MOUSE_LEFT, new AimToTargetInteractivity(TargetSnapMode.VALID) {

                @Override
                public boolean isValid(BoardObject boardObject) {
                    return (getAttackEvent(boardObject) != null);
                }

                @Override
                public void trigger(BoardObject source, BoardObject target) {
                    gameService.sendAction(getAttackEvent(target));
                }

                private AttackEvent getAttackEvent(BoardObject targetBoardObject) {
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
                        for (AttackEvent attackEvent : attackEventsOfSource) {
                            if (attackEvent.target == targetEntity) {
                                return attackEvent;
                            }
                        }
                    }
                    return null;
                }
            });
            attackingCard.getModel().setPlayable(true);
        }
    }
}
