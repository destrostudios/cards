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
        LinkedList<Integer> coveredSpells = new LinkedList<>();
        for (Event event : possibleEvents) {
            if (event instanceof PlaySpellEvent playSpellEvent) {
                if (!coveredSpells.contains(playSpellEvent.spell)) {
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
                                Integer targetEntity = getEntity(boardObject);
                                if (targetEntity != null) {
                                    return SpellUtil.isCastable(data, cardEntity, playSpellEvent.spell, new int[] { targetEntity });
                                }
                                return false;
                            }

                            @Override
                            public void trigger(BoardObject boardObject, BoardObject target) {
                                int targetEntity = getEntity(target);
                                gameService.sendAction(new PlaySpellEvent(playSpellEvent.spell, new int[] { targetEntity }));
                            }

                            private Integer getEntity(BoardObject<?> boardObject) {
                                if (boardObject instanceof Card card) {
                                    return cardGuiMap.getEntity(card);
                                } else if (boardObject instanceof CardZone cardZone) {
                                    for (Map.Entry<Integer, PlayerZones> playerZonesEntry : playerZonesMap.entrySet()) {
                                        int playerEntity = playerZonesEntry.getKey();
                                        PlayerZones playerZones = playerZonesEntry.getValue();
                                        if (cardZone == playerZones.getHandZone()) {
                                            return playerEntity;
                                        }
                                    }
                                }
                                return null;
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
                    boolean isDefaultSpell = (SpellUtil.isDefaultCastFromHandSpell(data, playSpellEvent.spell) || SpellUtil.isDefaultAttackSpell(data, playSpellEvent.spell));
                    InteractivitySource interactivitySource = (isDefaultSpell ? InteractivitySource.MOUSE_LEFT : InteractivitySource.MOUSE_RIGHT);
                    card.setInteractivity(interactivitySource, interactivity);
                    card.getModel().setPlayable(true);

                    coveredSpells.add(playSpellEvent.spell);
                }
            }
        }
    }
}
