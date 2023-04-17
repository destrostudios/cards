package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.boardobjects.TargetArrow;
import com.destrostudios.cardgui.events.MoveCardEvent;
import com.destrostudios.cardgui.samples.boardobjects.targetarrow.*;
import com.destrostudios.cardgui.samples.animations.*;
import com.destrostudios.cardgui.samples.visualization.*;
import com.destrostudios.cardgui.transformations.ConstantButTargetedTransformation;
import com.destrostudios.cardgui.transformations.speeds.TimeBasedRotationTransformationSpeed;
import com.destrostudios.cardgui.transformations.speeds.TimeBasedVectorTransformationSpeed3f;
import com.destrostudios.cardgui.zones.*;
import com.destrostudios.cards.frontend.application.*;
import com.destrostudios.cards.frontend.application.appstates.boards.*;
import com.destrostudios.cards.frontend.application.appstates.services.*;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.players.PlayerBoardObject;
import com.destrostudios.cards.frontend.application.appstates.services.players.PlayerVisualizer;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.EventType;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.PlayerActionsGenerator;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.zones.*;
import com.destrostudios.cards.shared.rules.game.*;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.List;

public class IngameAppState extends MyBaseAppState implements ActionListener {

    public IngameAppState(GameService gameService) {
        this.gameService = gameService;
        entityBoardMap = new EntityBoardMap(gameService.getGameContext().getData());
        playerActionsGenerator = new PlayerActionsGenerator();
    }
    private static final float ZONE_HEIGHT = 1.3f;
    private GameService gameService;
    private int initState;
    private Board board;
    private CenteredIntervalZone selectionZone;
    private SimpleIntervalZone inspectionZone;
    private Card<CardModel> inspectionCard;
    private HashMap<Integer, PlayerZones> playerZonesMap = new HashMap<>();
    private EntityBoardMap entityBoardMap;
    private PlayerActionsGenerator playerActionsGenerator;
    private UpdateBoardService updateBoardService;
    private EntryAnimationService entryAnimationService;
    private Event sendableEndTurnEvent;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        initCamera();
        mainApplication.getStateManager().attach(new ForestBoardAppState());
        initBoard();
        initInputListeners();
    }

    private void initCamera() {
        // TODO: Cleanup (No anonymous class)
        mainApplication.getStateManager().attach(new CameraAppState(){

            @Override
            public void initialize(AppStateManager stateManager, Application application) {
                super.initialize(stateManager, application);
                Vector3f position = new Vector3f(0, 7.361193f, 1.3f);
                Quaternion rotation = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
                rotation = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y).multLocal(rotation);
                moveTo(position, rotation, 0.3f);
            }
        });
    }

    private void initBoard() {
        // Board
        board = new Board(BoardSettings.builder()
                .inputActionPrefix("ingame")
                .cardInZonePositionTransformationSpeed(() -> new TimeBasedVectorTransformationSpeed3f(0.8f))
                .cardInZoneRotationTransformationSpeed(() -> new TimeBasedRotationTransformationSpeed(0.4f))
                .cardInZonePositionTransformationSpeed(() -> new TimeBasedVectorTransformationSpeed3f(0.8f))
                .dragProjectionZ(0.996f)
                .hoverInspectionDelay(0f)
                .isInspectable(this::isInspectable)
                .inspector(new Inspector() {

                    @Override
                    public void inspect(BoardAppState boardAppState, TransformedBoardObject<?> transformedBoardObject, Vector3f vector3f) {
                        Card<CardModel> card = (Card<CardModel>) transformedBoardObject;
                        board.triggerEvent(new MoveCardEvent(inspectionCard, inspectionZone, new Vector3f()));
                        inspectionCard.finishTransformations();
                        inspectionCard.getModel().set(card.getModel());
                    }

                    @Override
                    public boolean isReadyToUninspect() {
                        return true;
                    }

                    @Override
                    public void uninspect() {
                        board.unregister(inspectionCard);
                    }
                })
                .build());

        // Zones & BoardObjects

        selectionZone = new CenteredIntervalZone(new Vector3f(0, 3.8f, 1.32f), Quaternion.IDENTITY, new Vector3f(0.83f, 1, 1));
        board.addZone(selectionZone);

        inspectionZone = new SimpleIntervalZone(new Vector3f(-1.29f, 5, 1.672f), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        board.addZone(inspectionZone);
        inspectionCard = new Card<>(new CardModel());

        Vector3f offset = new Vector3f(0, 0, ZONE_HEIGHT);
        float directionX = 1;
        float directionZ = 1;
        // TODO: Properly query these, ensure own player is first
        int[] players = new int[2];
        players[0] = gameService.getPlayerEntity();
        players[1] = ((players[0] == 0) ? 1 : 0);
        for (int i = 0; i < players.length; i++) {
            if (i == 1) {
                directionX *= -1;
                directionZ *= -1;
            }

            float z = directionZ * 1.7f;
            // Vertical centering
            z += i * -0.15f;
            PlayerBoardObject playerBoardObject = entityBoardMap.getOrCreatePlayer(players[i]);
            playerBoardObject.position().setTransformation(new ConstantButTargetedTransformation<>(offset.add(0, 0.01f, z)));
            board.register(playerBoardObject);

            z = 0.2f + (ZONE_HEIGHT / 2);
            CenteredIntervalZone creatureZone = new CenteredIntervalZone(offset.add(0, 0, directionZ * z), Quaternion.IDENTITY, new Vector3f(1, 1, 1));
            float x = 4.5f;
            SimpleIntervalZone graveyardZone = new SimpleIntervalZone(offset.add(directionX * x, 0, directionZ * z), Quaternion.IDENTITY, new Vector3f(0.02f, 1, 1)) {

                // TODO: Cleanup
                @Override
                public Vector3f getLocalCardPosition(Vector3f zonePosition) {
                    Vector3f localPosition = super.getLocalCardPosition(zonePosition);
                    return new Vector3f(localPosition.y, localPosition.x, localPosition.z);
                }
            };

            z += ZONE_HEIGHT;
            SimpleIntervalZone deckZone = new SimpleIntervalZone(offset.add(directionX * x, 0, directionZ * z), Quaternion.IDENTITY, new Vector3f(0.015f, 0, 0)) {

                // TODO: Cleanup
                @Override
                public Vector3f getLocalCardPosition(Vector3f zonePosition) {
                    Vector3f localPosition = super.getLocalCardPosition(zonePosition);
                    return new Vector3f(localPosition.y, localPosition.x, localPosition.z);
                }
            };

            z += 0.5f;
            Quaternion handRotation = new Quaternion().fromAngleAxis(0.1f * FastMath.PI, Vector3f.UNIT_X);
            CenteredIntervalZone handZone = new CenteredIntervalZone(offset.add(0, 0.25f, directionZ * z), handRotation, new Vector3f(0.85f, 1, 1));

            board.addZone(deckZone);
            board.addZone(handZone);
            board.addZone(creatureZone);
            board.addZone(graveyardZone);
            playerZonesMap.put(players[i], new PlayerZones(deckZone, handZone, creatureZone, graveyardZone));
        }

        // Visualizers

        board.registerVisualizer_Class(PlayerBoardObject.class, new PlayerVisualizer());
        // TODO: Offer this kind of ZoneVisualizer out of the box from the cardgui
        board.registerVisualizer_Class(CardZone.class, new DebugZoneVisualizer() {

            @Override
            protected Geometry createVisualizationObject(AssetManager assetManager) {
                Geometry geometry = super.createVisualizationObject(assetManager);
                geometry.setCullHint(Spatial.CullHint.Always);
                return geometry;
            }

            @Override
            protected Vector2f getSize(CardZone zone) {
                for (PlayerZones playerZones : playerZonesMap.values()) {
                    if (zone == playerZones.getDeckZone()) {
                        return new Vector2f(1, ZONE_HEIGHT);
                    } else if (zone == playerZones.getHandZone()) {
                        return new Vector2f(5, ZONE_HEIGHT - 0.1f);
                    } else if (zone == playerZones.getCreatureZone()) {
                        return new Vector2f(6.5f, ZONE_HEIGHT);
                    } else if (zone == playerZones.getGraveyardZone()) {
                        return new Vector2f(1, ZONE_HEIGHT);
                    }
                }
                return super.getSize(zone);
            }
        });
        board.registerVisualizer_ZonePosition(zonePosition -> {
            for (PlayerZones playerZones : playerZonesMap.values()) {
                if (zonePosition.getZone() == playerZones.getCreatureZone()) {
                    return true;
                }
            }
            return false;
        }, new IngameCardVisualizer(true, true));
        board.registerVisualizer_ZonePosition(zonePosition -> {
            for (PlayerZones playerZones : playerZonesMap.values()) {
                if (zonePosition.getZone() == playerZones.getHandZone()) {
                    return true;
                }
            }
            return false;
        }, new IngameCardVisualizer(true, false));
        board.registerVisualizer_ZonePosition(
            zonePosition -> (zonePosition.getZone() == selectionZone) || (zonePosition.getZone() == inspectionZone),
            new IngameCardVisualizer(false, false, 1, false)
        );
        board.registerVisualizer_Class(Card.class, new IngameCardVisualizer(false, false));
        board.registerVisualizer_Class(TargetArrow.class, new SimpleTargetArrowVisualizer(SimpleTargetArrowSettings.builder()
                .width(0.5f)
                .build()));

        // BoardAppState
        mainApplication.getStateManager().attach(new BoardAppState(board, mainApplication.getRootNode()));

        // Services

        updateBoardService = new UpdateBoardService(gameService, board, selectionZone, playerZonesMap, entityBoardMap);
        entryAnimationService = new EntryAnimationService(mainApplication);

        // Events

        gameService.getGameContext().getEventHandling().resolved().add(EventType.MOVE_TO_CREATURE_ZONE, (MoveToCreatureZoneEvent event, GameContext context) -> {
            tryPlayEntryAnimation(event.card);
        });
        gameService.getGameContext().getEventHandling().pre().add(EventType.BATTLE, (BattleEvent event, GameContext context) -> {
            TransformedBoardObject<?> attacker = entityBoardMap.getBoardObject(event.source);
            TransformedBoardObject<?> defender = entityBoardMap.getBoardObject(event.target);
            board.playAnimation(new AttackAnimation(attacker, defender, 0.5f));
        });
        gameService.getGameContext().getEventHandling().resolved().add(EventType.BATTLE, (event, context) -> {
            board.playAnimation(new CameraShakeAnimation(mainApplication.getCamera(), 0.4f, 0.005f));
        });
        /*gameService.getGameContext().getEventHandling().pre().add(EventType.SHUFFLE_LIBRARY, (ShuffleLibraryEvent event, GameContext context) -> {
            LinkedList<Card> libraryCards = playerZonesMap.get(event.player).getDeckZone().getCards();
            board.playAnimation(new ShuffleAnimation(libraryCards, mainApplication));
        });*/

        gameService.getGameContext().getEventHandling().instant().add(EventType.GAME_OVER, (GameOverEvent event, GameContext context) -> {
            gameService.onGameOver();
            boolean isWinner = (gameService.getPlayerEntity() == event.winner);
            mainApplication.getStateManager().attach(new GameOverAppState(isWinner));
        });
    }

    private boolean isInspectable(TransformedBoardObject<?> transformedBoardObject) {
        if (transformedBoardObject instanceof Card card) {
            CardModel cardModel = (CardModel) card.getModel();
            return cardModel.isFront() && (card.getZonePosition().getZone() != selectionZone);
        }
        return false;
    }

    private void tryPlayEntryAnimation(int cardEntity) {
        mainApplication.enqueue(() -> {
            Card<CardModel> card = entityBoardMap.getOrCreateCard(cardEntity);
            Animation entryAnimation = entryAnimationService.getEntryAnimation(card);
            if (entryAnimation != null) {
                board.playAnimation(entryAnimation);
            }
        });
    }

    private void initInputListeners() {
        InputManager inputManager = mainApplication.getInputManager();
        inputManager.addMapping("space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(this, "space", "1");
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        if (initState == 0) {
            // Board is initialized now
            updateVisuals();
            board.finishAllTransformations();
            initState++;
        } else if ((initState == 1) && (!gameService.getGameContext().isGameOver())) {
            processNextEvents();
        }
    }

    private void processNextEvents() {
        EventQueue<GameContext> eventQueue = gameService.getGameContext().getEvents();
        if (!eventQueue.hasPendingEventHandler()) {
            gameService.applyNextActionIfExisting();
        }
        if (eventQueue.hasPendingEventHandler()) {
            while (!board.isAnimationPlaying()) {
                eventQueue.triggerNextEventHandler(gameService.getGameContext());
                if (board.isAnimationPlaying()) {
                    updateBoardService.update(null);
                }
                if (!eventQueue.hasPendingEventHandler()) {
                    updateVisuals();
                    break;
                }
            }
        }
    }

    private void updateVisuals() {
        List<Event> possibleEvents;
        sendableEndTurnEvent = null;
        if (gameService.getGameContext().isGameOver()) {
            possibleEvents = null;
        } else {
            possibleEvents = playerActionsGenerator.generatePossibleActions(gameService.getGameContext().getData(), gameService.getPlayerEntity());
            for (Event event : possibleEvents) {
                if (event instanceof EndTurnEvent) {
                    sendableEndTurnEvent = event;
                }
            }
        }
        updateBoardService.update(possibleEvents);
    }

    @Override
    public void onAction(String name, boolean isPressed, float lastTimePerFrame) {
        if ("1".equals(name) && isPressed) {
            CameraAppState cameraAppState = getAppState(CameraAppState.class);
            cameraAppState.setFreeCameraEnabled(!cameraAppState.isFreeCameraEnabled());
        } else if (!gameService.getGameContext().isGameOver()) {
            if ("space".equals(name) && isPressed) {
                if (gameService.getGameContext().getData().hasComponent(gameService.getPlayerEntity(), Components.Player.MULLIGAN)) {
                    gameService.sendMulliganAction();
                } else if (sendableEndTurnEvent != null) {
                    gameService.sendAction(sendableEndTurnEvent);
                }
            }
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(CameraAppState.class));
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(ForestBoardAppState.class));
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(BoardAppState.class));
        mainApplication.getInputManager().deleteMapping("space");
        mainApplication.getInputManager().deleteMapping("1");
        mainApplication.getInputManager().removeListener(this);
    }
}
