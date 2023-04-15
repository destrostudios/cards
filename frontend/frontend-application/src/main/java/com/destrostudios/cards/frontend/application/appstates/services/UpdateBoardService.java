package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.events.*;
import com.destrostudios.cardgui.interactivities.*;
import com.destrostudios.cards.frontend.application.EntityBoardMap;
import com.destrostudios.cards.frontend.application.PlayerZones;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.players.PlayerBoardObject;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.jme3.math.Vector3f;

import java.util.*;

public class UpdateBoardService {

    public UpdateBoardService(GameService gameService, Board board, CardZone selectionZone, HashMap<Integer, PlayerZones> playerZonesMap, EntityBoardMap entityBoardMap) {
        this.gameService = gameService;
        this.board = board;
        this.selectionZone = selectionZone;
        this.playerZonesMap = playerZonesMap;
        this.entityBoardMap = entityBoardMap;
        validSpellTargets = new HashMap<>();
    }
    private GameService gameService;
    private Board board;
    private CardZone selectionZone;
    private HashMap<Integer, PlayerZones> playerZonesMap;
    private EntityBoardMap entityBoardMap;
    private HashMap<Integer, LinkedList<Integer>> validSpellTargets;

    public void update(List<Event> possibleEvents) {
        EntityData data = gameService.getGameContext().getData();
        IntList players = data.list(Components.NEXT_PLAYER);
        for (int player : players) {
            PlayerBoardObject playerBoardObject = entityBoardMap.getOrCreatePlayer(player);
            playerBoardObject.getModel().setActivePlayer(data.hasComponent(player, Components.Player.ACTIVE_PLAYER));
            playerBoardObject.getModel().setName(data.getComponent(player, Components.NAME));
            playerBoardObject.getModel().setCurrentHealth(StatsUtil.getEffectiveHealth(data, player));
            playerBoardObject.getModel().setMaxHealth(data.getComponent(player, Components.Stats.HEALTH));
            playerBoardObject.getModel().setCurrentMana(data.getOptionalComponent(player, Components.MANA).orElse(0));
            playerBoardObject.getModel().setMaxMana(data.getOptionalComponent(player, Components.AVAILABLE_MANA).orElse(0));

            PlayerZones playerZones = playerZonesMap.get(player);
            updateZoneCards(data, player, Components.Player.LIBRARY_CARDS, playerZones.getDeckZone());
            updateZoneCards(data, player, Components.Player.HAND_CARDS, playerZones.getHandZone());
            updateZoneCards(data, player, Components.Player.CREATURE_ZONE_CARDS, playerZones.getCreatureZone());
            updateZoneCards(data, player, Components.Player.GRAVEYARD_CARDS, playerZones.getGraveyardZone());
        }
        if (possibleEvents != null) {
            updateInteractivities(possibleEvents);
        }
    }

    private void updateZoneCards(EntityData data, int player, ComponentDefinition<IntList> playerZoneComponent, CardZone playerCardZone) {
        IntList cardEntities = data.getComponent(player, playerZoneComponent);
        for (int i = 0; i < cardEntities.size(); i++) {
            int cardZoneIndex = i;
            int cardEntity = cardEntities.get(i);
            CardZone cardZone = playerCardZone;
            boolean isFront = true;

            if (playerZoneComponent == Components.Player.LIBRARY_CARDS) {
                isFront = false;
            } else if (playerZoneComponent == Components.Player.HAND_CARDS) {
                if (player == gameService.getPlayerEntity()) {
                    if (data.hasComponent(player, Components.Player.MULLIGAN)) {
                        cardZone = selectionZone;
                    }
                } else {
                    isFront = false;
                }
            }

            Card<CardModel> card = entityBoardMap.getOrCreateCard(cardEntity);
            card.clearInteractivities();
            CardGuiMapper.updateModel(data, cardEntity, card.getModel(), isFront);

            board.triggerEvent(new MoveCardEvent(card, cardZone, new Vector3f(cardZoneIndex, 0, 0)));
        }
    }

    private void updateInteractivities(List<Event> possibleEvents) {
        EntityData data = gameService.getGameContext().getData();
        validSpellTargets.clear();
        int player = gameService.getPlayerEntity();
        if (data.hasComponent(player, Components.Player.ACTIVE_PLAYER)) {
            if (data.hasComponent(player, Components.Player.MULLIGAN)) {
                IntList handCards = data.list(Components.HAND, card -> data.getComponent(card, Components.OWNED_BY) == player);
                for (int cardEntity : handCards) {
                    Card<CardModel> card = entityBoardMap.getOrCreateCard(cardEntity);
                    card.setInteractivity(InteractivitySource.MOUSE_LEFT, new ClickInteractivity() {

                        @Override
                        public void trigger(BoardObject source, BoardObject target) {
                            boolean mulliganed = gameService.toggleMulliganCard(cardEntity);
                            card.getModel().setCrossed(mulliganed);
                        }
                    });
                    card.getModel().setPlayable(true);
                }
            } else {
                for (Event event : possibleEvents) {
                    if (event instanceof CastSpellEvent castSpellEvent) {
                        LinkedList<Integer> validTargets = validSpellTargets.computeIfAbsent(castSpellEvent.spell, s -> new LinkedList<>());
                        if (validTargets.isEmpty()) {
                            // TODO: Improve?
                            int cardEntity = data.list(Components.SPELLS, currentCardEntity -> ArrayUtil.contains(data.getComponent(currentCardEntity, Components.SPELLS), castSpellEvent.spell)).get(0);
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
                                        int targetEntity = getEntity(target);
                                        gameService.sendAction(new CastSpellEvent(cardEntity, castSpellEvent.spell, new int[] { targetEntity }));
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
    }
}
