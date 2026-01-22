package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.events.*;
import com.destrostudios.cardgui.interactivities.*;
import com.destrostudios.cards.frontend.application.EntityBoardMap;
import com.destrostudios.cards.frontend.application.PlayerZones;
import com.destrostudios.cards.frontend.application.appstates.ClickCardSelectorAppState;
import com.destrostudios.cards.frontend.application.appstates.SelectorAppState;
import com.destrostudios.cards.frontend.application.appstates.TargetsScrollSelectorAppState;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.players.PlayerBoardObject;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.PlayerActionsGenerator;
import com.destrostudios.cards.shared.rules.actions.Action;
import com.destrostudios.cards.shared.rules.actions.CastSpellAction;
import com.destrostudios.cards.shared.rules.actions.EndTurnAction;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.shared.rules.effects.EffectOptions;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.cards.shared.rules.util.CardUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.jme3.math.Vector3f;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

public class UpdateIngameService {

    public UpdateIngameService(GameService gameService, Board board, CardZone selectionZone, HashMap<Integer, PlayerZones> playerZonesMap, EntityBoardMap entityBoardMap, IngameGuiService ingameGuiService, Consumer<SelectorAppState> openCardSelector) {
        this.gameService = gameService;
        this.board = board;
        this.selectionZone = selectionZone;
        this.playerZonesMap = playerZonesMap;
        this.entityBoardMap = entityBoardMap;
        this.ingameGuiService = ingameGuiService;
        this.openCardSelector = openCardSelector;
        validSpellTargets = new HashMap<>();
    }
    private GameService gameService;
    private Board board;
    private CardZone selectionZone;
    private HashMap<Integer, PlayerZones> playerZonesMap;
    private EntityBoardMap entityBoardMap;
    private IngameGuiService ingameGuiService;
    private Consumer<SelectorAppState> openCardSelector;
    @Getter
    private Action sendableEndTurnAction;
    private HashMap<Integer, ArrayList<int[]>> validSpellTargets;

    public void update(boolean interactive) {
        List<Action> possibleActions = null;
        sendableEndTurnAction = null;
        if (interactive) {
            if (gameService.getGameContext().isGameOver()) {
                possibleActions = null;
            } else {
                possibleActions = PlayerActionsGenerator.generatePossibleActions(gameService.getGameContext().getData(), gameService.getPlayerEntity());
                for (Action action : possibleActions) {
                    if (action instanceof EndTurnAction) {
                        sendableEndTurnAction = action;
                        break;
                    }
                }
            }
        }
        updateBoard(possibleActions);
        ingameGuiService.update(sendableEndTurnAction != null);
    }

    private void updateBoard(List<Action> possibleActions) {
        EntityData data = gameService.getGameContext().getData();
        IntList players = data.list(Components.NEXT_PLAYER);
        for (int player : players) {
            PlayerBoardObject playerBoardObject = entityBoardMap.getOrCreatePlayer(player);
            playerBoardObject.getModel().setActivePlayer(data.hasComponent(player, Components.Player.ACTIVE_PLAYER));
            playerBoardObject.getModel().setName(data.getComponent(player, Components.NAME));
            playerBoardObject.getModel().setCurrentHealth(StatsUtil.getEffectiveHealth(data, player));
            playerBoardObject.getModel().setMaxHealth(data.getComponent(player, Components.Stats.HEALTH));
            playerBoardObject.getModel().setCurrentMana(data.getComponentOrElse(player, Components.MANA, 0));
            playerBoardObject.getModel().setMaxMana(data.getComponentOrElse(player, Components.AVAILABLE_MANA, 0));

            PlayerZones playerZones = playerZonesMap.get(player);
            updateZoneCards(data, player, Components.Player.POWER_CARDS, playerZones.getPowerZone());
            updateZoneCards(data, player, Components.Player.LIBRARY_CARDS, playerZones.getDeckZone());
            updateZoneCards(data, player, Components.Player.HAND_CARDS, playerZones.getHandZone());
            updateZoneCards(data, player, Components.Player.CREATURE_ZONE_CARDS, playerZones.getCreatureZone());
            updateZoneCards(data, player, Components.Player.GRAVEYARD_CARDS, playerZones.getGraveyardZone());
        }
        if (possibleActions != null) {
            updateInteractivities(possibleActions);
        }
    }

    private void updateZoneCards(EntityData data, int player, ComponentDefinition<IntList> playerZoneComponent, CardZone playerCardZone) {
        IntList cardEntities = data.getComponent(player, playerZoneComponent);
        for (int i = 0; i < cardEntities.size(); i++) {
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

            board.triggerEvent(new MoveCardEvent(card, cardZone, new Vector3f(i, 0, 0)));
        }
    }

    private void updateInteractivities(List<Action> possibleActions) {
        EntityData data = gameService.getGameContext().getData();
        validSpellTargets.clear();
        int player = gameService.getPlayerEntity();
        if (data.hasComponent(player, Components.Player.ACTIVE_PLAYER)) {
            if (data.hasComponent(player, Components.Player.MULLIGAN)) {
                IntList handCards = data.list(Components.Zone.PLAYER_HAND[player]);
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
                for (Action action : possibleActions) {
                    if (action instanceof CastSpellAction castSpellAction) {
                        int source = castSpellAction.getSource();
                        int spell = castSpellAction.getSpell();
                        int[] targets = castSpellAction.getTargets();

                        ArrayList<int[]> validTargets = validSpellTargets.computeIfAbsent(spell, _ -> new ArrayList<>());
                        if (validTargets.isEmpty()) {
                            Card<CardModel> card = entityBoardMap.getOrCreateCard(source);

                            DiscoverPool discoverPool = SpellUtil.getDiscoverEffectPool(data, spell);
                            Runnable openDiscoverSelector = () -> {

                                EntityData discoverData = new SimpleEntityData(Components.ALL);
                                String[] templates = gameService.getGameContext().getDiscoverTemplates(discoverPool);

                                ArrayList<CardModel> discoverCardModels = new ArrayList<>();
                                for (int i = 0; i < GameConstants.DISCOVER_OPTIONS; i++) {
                                    String template = templates[i];
                                    int discoverCardEntity = CardUtil.createCard(discoverData, template, data.getComponent(source, Components.FOIL));
                                    CardModel cardModel = new CardModel();
                                    CardGuiMapper.updateModel(discoverData, discoverCardEntity, cardModel, true);
                                    cardModel.setPlayable(true);
                                    discoverCardModels.add(cardModel);
                                }

                                String description = data.getComponent(source, Components.DESCRIPTION);
                                update(false);
                                ingameGuiService.setAttached(false);
                                openCardSelector.accept(new ClickCardSelectorAppState(description, discoverCardModels, null, false, selectedTarget -> {
                                    int discoverIndex = discoverCardModels.indexOf(selectedTarget);
                                    gameService.sendAction(new CastSpellAction(source, spell, ArrayUtil.EMPTY, EffectOptions.builder().discoverIndex(discoverIndex).build()));
                                    ingameGuiService.setAttached(true);
                                }));
                            };

                            Runnable openScrollSelector = () -> {
                                String description = data.getComponent(source, Components.DESCRIPTION);
                                update(false);
                                ingameGuiService.setAttached(false);
                                openCardSelector.accept(new TargetsScrollSelectorAppState(description, validTargets, t -> entityBoardMap.getOrCreateCard(t).getModel(), () -> {
                                    update(true);
                                    ingameGuiService.setAttached(true);
                                }, selectedTargets -> {
                                    gameService.sendAction(new CastSpellAction(source, spell, selectedTargets, null));
                                    ingameGuiService.setAttached(true);
                                }));
                            };

                            Interactivity interactivity;
                            if ((castSpellAction.getTargets().length > 0) && SpellUtil.isTargeted(data, castSpellAction.getSpell())) {
                                if ((castSpellAction.getTargets().length == 1) && SpellUtil.isTargetingBoard(data, castSpellAction.getSpell())) {
                                    interactivity = new AimToTargetInteractivity(TargetSnapMode.VALID) {

                                        @Override
                                        public boolean isValid(BoardObject boardObject) {
                                            Integer target = getEntity(boardObject);
                                            return ((target != null) && validTargets.stream().anyMatch(targets -> targets[0] == target));
                                        }

                                        @Override
                                        public void trigger(BoardObject boardObject, BoardObject target) {
                                            int targetEntity = getEntity(target);
                                            gameService.sendAction(new CastSpellAction(source, spell, new int[]{ targetEntity }, null));
                                        }

                                        private Integer getEntity(BoardObject<?> boardObject) {
                                            if (boardObject instanceof TransformedBoardObject<?> transformedBoardObject) {
                                                return entityBoardMap.getEntity(transformedBoardObject);
                                            }
                                            return null;
                                        }
                                    };
                                } else if (data.hasComponent(source, Components.POWER_CARD)) {
                                    interactivity = new ClickInteractivity() {

                                        @Override
                                        public void trigger(BoardObject boardObject, BoardObject target) {
                                            openScrollSelector.run();
                                        }
                                    };
                                } else {
                                    interactivity = new DragToPlayInteractivity() {

                                        @Override
                                        public void trigger(BoardObject boardObject, BoardObject target) {
                                            openScrollSelector.run();
                                        }
                                    };
                                }
                            } else if (data.hasComponent(source, Components.Zone.HAND)) {
                                interactivity = new DragToPlayInteractivity() {

                                    @Override
                                    public void trigger(BoardObject boardObject, BoardObject target) {
                                        if (discoverPool != null) {
                                            openDiscoverSelector.run();
                                        } else {
                                            gameService.sendAction(castSpellAction);
                                        }
                                    }
                                };
                            } else {
                                interactivity = new ClickInteractivity() {

                                    @Override
                                    public void trigger(BoardObject boardObject, BoardObject target) {
                                        if (discoverPool != null) {
                                            openDiscoverSelector.run();
                                        } else {
                                            gameService.sendAction(castSpellAction);
                                        }
                                    }
                                };
                            }
                            boolean isDefaultSpell = (SpellUtil.isDefaultPlaySpell(data, spell) || SpellUtil.isDefaultAttackSpell(data, spell));
                            InteractivitySource interactivitySource = (isDefaultSpell ? InteractivitySource.MOUSE_LEFT : InteractivitySource.MOUSE_RIGHT);
                            card.setInteractivity(interactivitySource, interactivity);
                            card.getModel().setPlayable(true);
                        }
                        validTargets.add(targets);
                    }
                }
            }
        }
    }
}
