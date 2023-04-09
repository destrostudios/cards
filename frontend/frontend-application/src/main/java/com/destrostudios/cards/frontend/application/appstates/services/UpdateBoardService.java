package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.events.*;
import com.destrostudios.cardgui.interactivities.*;
import com.destrostudios.cards.frontend.application.EntityBoardMap;
import com.destrostudios.cards.frontend.application.PlayerZones;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.players.PlayerBoardObject;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.jme3.math.Vector3f;

import java.util.*;
import java.util.stream.IntStream;

public class UpdateBoardService {

    public UpdateBoardService(GameService gameService, Board board, HashMap<Integer, PlayerZones> playerZonesMap, EntityBoardMap entityBoardMap) {
        this.gameService = gameService;
        this.board = board;
        this.playerZonesMap = playerZonesMap;
        this.entityBoardMap = entityBoardMap;
        validSpellTargets = new HashMap<>();
    }
    private GameService gameService;
    private Board board;
    private HashMap<Integer, PlayerZones> playerZonesMap;
    private EntityBoardMap entityBoardMap;
    private HashMap<Integer, LinkedList<Integer>> validSpellTargets;

    public void update(List<Event> possibleEvents) {
        EntityData data = gameService.getGameContext().getData();
        List<Integer> players = data.query(Components.NEXT_PLAYER).list();
        for (int player : players) {
            PlayerBoardObject playerBoardObject = entityBoardMap.getOrCreatePlayer(player);
            playerBoardObject.getModel().setActivePlayer(data.hasComponent(player, Components.Game.ACTIVE_PLAYER));
            playerBoardObject.getModel().setName(data.getComponent(player, Components.NAME));
            playerBoardObject.getModel().setCurrentHealth(StatsUtil.getEffectiveHealth(data, player));
            playerBoardObject.getModel().setMaxHealth(data.getComponent(player, Components.Stats.HEALTH));
            playerBoardObject.getModel().setCurrentMana(data.getOptionalComponent(player, Components.MANA).orElse(0));
            playerBoardObject.getModel().setMaxMana(data.getOptionalComponent(player, Components.AVAILABLE_MANA).orElse(0));
        }
        List<Integer> cardEntities = data.query(Components.OWNED_BY).list();
        for (int cardEntity : cardEntities) {
            CardZone cardZone = null;
            Integer cardZoneIndex;
            boolean isFront = true;

            int owner = data.getComponent(cardEntity, Components.OWNED_BY);
            PlayerZones playerZones = playerZonesMap.get(owner);

            // TODO: Yeah...
            cardZoneIndex = data.getComponent(cardEntity, Components.LIBRARY);
            if (cardZoneIndex != null) {
                cardZone = playerZones.getDeckZone();
                isFront = false;
            } else {
                cardZoneIndex = data.getComponent(cardEntity, Components.HAND);
                if (cardZoneIndex != null) {
                    cardZone = playerZones.getHandZone();
                    if (owner != gameService.getPlayerEntity()) {
                        isFront = false;
                    }
                } else {
                    cardZoneIndex = data.getComponent(cardEntity, Components.CREATURE_ZONE);
                    if (cardZoneIndex != null) {
                        cardZone = playerZones.getCreatureZone();
                    } else {
                        cardZoneIndex = data.getComponent(cardEntity, Components.GRAVEYARD);
                        if (cardZoneIndex != null) {
                            cardZone = playerZones.getGraveyardZone();
                        }
                    }
                }
            }

            Card<CardModel> card = entityBoardMap.getOrCreateCard(cardEntity);
            card.clearInteractivities();
            CardGuiMapper.updateModel(data, cardEntity, card.getModel(), isFront);
            board.triggerEvent(new MoveCardEvent(card, cardZone, new Vector3f(cardZoneIndex, 0, 0)));
        }
        if (possibleEvents != null) {
            updateInteractivities(possibleEvents);
        }
    }

    private void updateInteractivities(List<Event> possibleEvents) {
        EntityData data = gameService.getGameContext().getData();
        validSpellTargets.clear();
        for (Event event : possibleEvents) {
            if (event instanceof CastSpellEvent castSpellEvent) {
                LinkedList<Integer> validTargets = validSpellTargets.computeIfAbsent(castSpellEvent.spell, s -> new LinkedList<>());
                if (validTargets.isEmpty()) {
                    // TODO: Improve?
                    int cardEntity = data.query(Components.SPELLS)
                            .unique(currentCardEntity -> IntStream.of(data.getComponent(currentCardEntity, Components.SPELLS))
                                    .anyMatch(entity -> entity == castSpellEvent.spell)).getAsInt();
                    Card<CardModel> card = entityBoardMap.getOrCreateCard(cardEntity);

                    Interactivity interactivity;
                    if (SpellUtil.isTargeted(data, castSpellEvent.spell) && (castSpellEvent.targets.length > 0)) {
                        interactivity = new AimToTargetInteractivity(TargetSnapMode.VALID) {

                            @Override
                            public boolean isValid(BoardObject boardObject) {
                                Integer target = getEntity(boardObject);
                                return ((target != null) && validTargets.contains(target));
                            }

                            @Override
                            public void trigger(BoardObject source, BoardObject target) {
                                int sourceEntity = getEntity(source);
                                int targetEntity = getEntity(target);
                                gameService.sendAction(new CastSpellEvent(sourceEntity, castSpellEvent.spell, new int[] { targetEntity }));
                            }

                            private Integer getEntity(BoardObject<?> boardObject) {
                                if (boardObject instanceof TransformedBoardObject transformedBoardObject) {
                                    return entityBoardMap.getEntity(transformedBoardObject);
                                }
                                return null;
                            }
                        };
                    } else if (data.hasComponent(cardEntity, Components.HAND)) {
                        interactivity = new DragToPlayInteractivity() {

                            @Override
                            public void trigger(BoardObject boardObject, BoardObject target) {
                                gameService.sendAction(castSpellEvent);
                            }
                        };
                    } else {
                        interactivity = new ClickInteractivity() {

                            @Override
                            public void trigger(BoardObject boardObject, BoardObject target) {
                                gameService.sendAction(castSpellEvent);
                            }
                        };
                    }
                    boolean isDefaultSpell = (SpellUtil.isDefaultCastFromHandSpell(data, castSpellEvent.spell) || SpellUtil.isDefaultAttackSpell(data, castSpellEvent.spell));
                    InteractivitySource interactivitySource = (isDefaultSpell ? InteractivitySource.MOUSE_LEFT : InteractivitySource.MOUSE_RIGHT);
                    card.setInteractivity(interactivitySource, interactivity);
                    card.getModel().setPlayable(true);
                }
                for (int target : castSpellEvent.targets) {
                    validTargets.add(target);
                }
            }
        }
    }
}
