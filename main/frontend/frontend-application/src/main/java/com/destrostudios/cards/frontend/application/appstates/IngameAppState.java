package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.boardobjects.TargetArrow;
import com.destrostudios.cardgui.samples.boardobjects.targetarrow.*;
import com.destrostudios.cardgui.samples.animations.*;
import com.destrostudios.cardgui.samples.visualization.*;
import com.destrostudios.cardgui.transformations.speeds.TimeBasedRotationTransformationSpeed;
import com.destrostudios.cardgui.transformations.speeds.TimeBasedVectorTransformationSpeed3f;
import com.destrostudios.cardgui.zones.*;
import com.destrostudios.cards.frontend.application.*;
import com.destrostudios.cards.frontend.application.appstates.boards.*;
import com.destrostudios.cards.frontend.application.appstates.services.*;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.cards.zones.*;
import com.destrostudios.cards.shared.rules.game.*;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.game.turn.StartTurnEvent;
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
import java.util.LinkedList;
import java.util.List;

public class IngameAppState extends MyBaseAppState implements ActionListener {

    public IngameAppState(SimpleGameClient gameClient) {
        this.gameClient = gameClient;
        cardGuiMap = new CardGuiMap(gameClient.getGame().getData());
    }
    private static final float ZONE_HEIGHT = 1.3f;
    private SimpleGameClient gameClient;
    private Board board;
    private HashMap<Integer, PlayerZones> playerZonesMap = new HashMap<>();
    private CardGuiMap cardGuiMap;
    private boolean hasPreparedBoard = false;
    private boolean isInitialized = false;
    private UpdateBoardService updateBoardService;
    private UpdateHudService updateHudService;
    private EntryAnimationService entryAnimationService;
    // TODO: Cleanup, solve better
    private Event sendableEndTurnEvent;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        initCamera();
        mainApplication.getStateManager().attach(new IngameHudAppState());
        initBoard();
        initInputListeners();
        // Enqueue so added appStates have been initialized
        mainApplication.enqueue(() -> gameClient.connect());
    }

    private void initCamera() {
        // TODO: Cleanup (No anonymous class)
        mainApplication.getStateManager().attach(new CameraAppState(){

            @Override
            public void initialize(AppStateManager stateManager, Application application) {
                super.initialize(stateManager, application);
                // TODO: Have a special transformation here (Will be used during shuffling animation and mulligan)
                updateCamera();
            }
        });
    }

    private void initInputListeners() {
        InputManager inputManager = mainApplication.getInputManager();
        inputManager.addMapping("space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("end", new KeyTrigger(KeyInput.KEY_END));
        inputManager.addMapping("delete", new KeyTrigger(KeyInput.KEY_DELETE));
        inputManager.addListener(this, "space", "end", "delete");
    }

    @Override
    public void onAction(String name, boolean isPressed, float lastTimePerFrame) {
        if ("space".equals(name) && isPressed) {
            CameraAppState cameraAppState = getAppState(CameraAppState.class);
            cameraAppState.setFreeCameraEnabled(!cameraAppState.isFreeCameraEnabled());
        } else if ("end".equals(name) && isPressed) {
            if (sendableEndTurnEvent != null) {
                gameClient.requestAction(sendableEndTurnEvent);
            }
        } // TODO: Acts as a temporary way to end the other players turn until the game initialization and test setup has been cleanuped
        else if ("delete".equals(name) && isPressed) {
            int activePlayerEntity = gameClient.getGame().getData().query(Components.Game.ACTIVE_PLAYER).unique().getAsInt();
            gameClient.requestAction(new EndTurnEvent(activePlayerEntity));
        }
    }

    private void initBoard() {
        board = new Board();
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
                    } else if (zone == playerZones.getSpellZone()) {
                        return new Vector2f(6.5f, ZONE_HEIGHT);
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
                if ((zonePosition.getZone() == playerZones.getHandZone())
                 || (zonePosition.getZone() == playerZones.getSpellZone())
                 || (zonePosition.getZone() == playerZones.getCreatureZone())) {
                    return true;
                }
            }
            return false;
        }, new IngameCardVisualizer(true));
        board.registerVisualizer_Class(Card.class, new IngameCardVisualizer(false));
        board.registerVisualizer_Class(TargetArrow.class, new SimpleTargetArrowVisualizer(SimpleTargetArrowSettings.builder()
                .width(0.5f)
                .build()));
        updateBoardService = new UpdateBoardService(gameClient, board, playerZonesMap, cardGuiMap);
        updateHudService = new UpdateHudService(gameClient, getAppState(IngameHudAppState.class));
        entryAnimationService = new EntryAnimationService(mainApplication);
        initGameListeners();
    }

    private void initGameListeners() {
        gameClient.addFullGameStateListener(entityData -> {
            List<Integer> players = entityData.query(Components.NEXT_PLAYER).list();
            Vector3f offset = new Vector3f(0, 0, ZONE_HEIGHT);
            float directionX = 1;
            float directionZ = 1;
            Quaternion zoneRotation = Quaternion.IDENTITY;
            for (int i = 0; i < players.size(); i++) {
                if (i == 1) {
                    directionX *= -1;
                    directionZ *= -1;
                    zoneRotation = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);
                }

                float x = -0.5f;
                float z = (ZONE_HEIGHT / 2);
                CenteredIntervalZone creatureZone = new CenteredIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(1, 1, 1));
                x += 5;
                SimpleIntervalZone graveyardZone = new SimpleIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(0.02f, 1, 1)) {

                    // TODO: Cleanup
                    @Override
                    public Vector3f getLocalCardPosition(Vector3f zonePosition) {
                        Vector3f localPosition = super.getLocalCardPosition(zonePosition);
                        return new Vector3f(localPosition.y, localPosition.x, localPosition.z);
                    }
                };

                x = -0.5f;
                z += ZONE_HEIGHT;
                CenteredIntervalZone spellZone = new CenteredIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(1, 1, 1));
                x += 5;
                SimpleIntervalZone deckZone = new SimpleIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(0.02f, 0, 0)) {

                    // TODO: Cleanup
                    @Override
                    public Vector3f getLocalCardPosition(Vector3f zonePosition) {
                        Vector3f localPosition = super.getLocalCardPosition(zonePosition);
                        return new Vector3f(localPosition.y, localPosition.x, localPosition.z);
                    }
                };

                x = 0;
                z += (ZONE_HEIGHT - 0.25f);
                Quaternion handRotation = zoneRotation.mult(new Quaternion().fromAngleAxis(0.1f * FastMath.PI, Vector3f.UNIT_X));
                CenteredIntervalZone handZone = new CenteredIntervalZone(offset.add(directionX * x, 0, directionZ * z), handRotation, new Vector3f(0.85f, 1, 1));

                board.addZone(deckZone);
                board.addZone(handZone);
                board.addZone(spellZone);
                board.addZone(creatureZone);
                board.addZone(graveyardZone);
                playerZonesMap.put(players.get(i), new PlayerZones(deckZone, handZone, spellZone, creatureZone, graveyardZone));
            }
            BoardSettings boardSettings = BoardSettings.builder()
                    .cardInZonePositionTransformationSpeed(() -> new TimeBasedVectorTransformationSpeed3f(0.8f))
                    .cardInZoneRotationTransformationSpeed(() -> new TimeBasedRotationTransformationSpeed(0.4f))
                    .cardInZonePositionTransformationSpeed(() -> new TimeBasedVectorTransformationSpeed3f(0.8f))
                    .dragProjectionZ(0.996f)
                    .hoverInspectionDelay(0.75f)
                    .isInspectable(this::isInspectable)
                    .build();
            BoardAppState boardAppState = new BoardAppState(board, mainApplication.getRootNode(), boardSettings);
            mainApplication.getStateManager().attach(boardAppState);
            updateBoard();
            mainApplication.enqueue(() -> {
                board.finishAllTransformations();
            });
            hasPreparedBoard = true;

            mainApplication.getStateManager().attach(new ForestBoardAppState(gameClient.getPlayerEntity()));
        });

        gameClient.getGame().getEvents().instant().add(StartTurnEvent.class, event -> onTurnStarted());

        gameClient.getGame().getEvents().resolved().add(AddCardToCreatureZoneEvent.class, event -> tryPlayEntryAnimation(event.card));
        gameClient.getGame().getEvents().pre().add(BattleEvent.class, event -> board.playAnimation(new CameraShakeAnimation(mainApplication.getCamera(), 0.4f, 0.005f)));
        gameClient.getGame().getEvents().pre().add(ShuffleLibraryEvent.class, event -> {
            LinkedList<Card> deckCards = playerZonesMap.get(event.player).getDeckZone().getCards();
            // board.playAnimation(new ShuffleAnimation(deckCards, mainApplication));
        });

        gameClient.getGame().getEvents().instant().add(GameOverEvent.class, event -> {
            boolean isWinner = (gameClient.getPlayerEntity() == event.winner);
            mainApplication.getStateManager().attach(new GameOverAppState(isWinner));
        });
    }

    private boolean isInspectable(TransformedBoardObject<?> transformedBoardObject) {
        if (transformedBoardObject instanceof Card) {
            Card<?> card = (Card<?>) transformedBoardObject;
            CardZone cardZone = card.getZonePosition().getZone();
            for (PlayerZones playerZones : playerZonesMap.values()) {
                if (cardZone == playerZones.getDeckZone()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void onTurnStarted() {
        EntityData entityData = gameClient.getGame().getData();
        int player = entityData.query(Components.Game.ACTIVE_PLAYER).unique().getAsInt();
        // TODO: Map (Currently, it's exactly entity 0 and 1)
        int playerIndex = player;
        IngameHudAppState ingameHudAppState = getAppState(IngameHudAppState.class);
        ingameHudAppState.setCurrentPlayer(playerIndex);
        updateCamera();
    }

    private void updateCamera() {
        CameraAppState cameraAppState = getAppState(CameraAppState.class);
        Vector3f position = new Vector3f(0, 7.361193f, 1.3f);
        Quaternion rotation = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
        // TODO: Maybe check this other than the exact hardcoded entity
        boolean isPlayer1 = (gameClient.getPlayerEntity() == 0);
        if (isPlayer1) {
            rotation = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y).multLocal(rotation);
        }
        cameraAppState.moveTo(position, rotation, 0.3f);
    }

    private void updateBoard() {
        mainApplication.enqueue(() -> {
            List<Event> possibleEvents = gameClient.getGame().getActionGenerator().generatePossibleActions(gameClient.getPlayerEntity());
            sendableEndTurnEvent = null;
            for (Event event : possibleEvents) {
                if (event instanceof EndTurnEvent) {
                    sendableEndTurnEvent = event;
                }
            }
            updateBoardService.update(possibleEvents);
            updateHudService.update();
        });
    }

    private void tryPlayEntryAnimation(int cardEntity) {
        mainApplication.enqueue(() -> {
            Card<CardModel> card = cardGuiMap.getOrCreateCard(cardEntity);
            Animation entryAnimation = entryAnimationService.getEntryAnimation(card);
            if (entryAnimation != null) {
                board.playAnimation(entryAnimation);
            }
        });
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        if ((!isInitialized) && hasPreparedBoard) {
            gameClient.markAsReady();
            isInitialized = true;
        }
        processNextEvents();
    }

    private void processNextEvents() {
        EventQueue eventQueue = gameClient.getGame().getEvents();
        if (eventQueue.hasNextTriggeredHandler()) {
            while (!board.isAnimationPlaying()) {
                eventQueue.triggerNextHandler();
                if (!eventQueue.hasNextTriggeredHandler()) {
                    updateBoard();
                    break;
                }
            }
        }
    }
}
