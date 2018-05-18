package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.*;
import com.destrostudios.cards.frontend.application.appstates.services.UpdateBoardService;
import com.destrostudios.cards.frontend.cardgui.*;
import com.destrostudios.cards.frontend.cardgui.animations.*;
import com.destrostudios.cards.frontend.cardgui.transformations.*;
import com.destrostudios.cards.frontend.cardgui.visualisation.*;
import com.destrostudios.cards.frontend.cardgui.zones.IntervalZone;
import com.destrostudios.cards.frontend.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.PlayerActionsGenerator;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.game.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class IngameAppState extends MyBaseAppState implements ActionListener {

    public IngameAppState(SimpleGameClient gameClient) {
        this.gameClient = gameClient;
    }
    private SimpleGameClient gameClient;
    private Board<CardModel> board;
    private HashMap<Integer, PlayerZones> playerZonesMap = new HashMap<>();
    private CardGuiMap cardGuiMap = new CardGuiMap();
    private boolean hasPreparedBoard = false;
    private boolean isInitialized = false;
    private UpdateBoardService updateBoardService;
    // TODO: Cleanup, solve better
    private EndTurnEvent sendableEndTurnEvent;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        initCamera();
        initListeners();
        initBoard();
        gameClient.connect();
    }

    private void initCamera() {
        Camera camera = mainApplication.getCamera();
        camera.setLocation(new Vector3f(2.584369f, 14.878008f, 0.86850137f));
        camera.setRotation(new Quaternion(-0.001344382f, 0.72532254f, -0.6884064f, -0.0014168395f));
        camera.setFrustumPerspective(45, (float) camera.getWidth() / camera.getHeight(), 0.01f, 1000f);
        FlyByCamera flyByCamera = mainApplication.getFlyByCamera();
        flyByCamera.setMoveSpeed(100);
        flyByCamera.setEnabled(false);
    }

    private void initListeners() {
        InputManager inputManager = mainApplication.getInputManager();
        inputManager.addMapping("space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("end", new KeyTrigger(KeyInput.KEY_END));
        inputManager.addMapping("delete", new KeyTrigger(KeyInput.KEY_DELETE));
        inputManager.addListener(this, "space", "end", "delete");
    }

    @Override
    public void onAction(String name, boolean isPressed, float lastTimePerFrame) {
        if ("space".equals(name) && isPressed) {
            FlyByCamera flyByCamera = mainApplication.getFlyByCamera();
            mainApplication.getInputManager().setCursorVisible(flyByCamera.isEnabled());
            flyByCamera.setEnabled(!flyByCamera.isEnabled());
        }
        else if ("end".equals(name) && isPressed) {
            if (sendableEndTurnEvent != null) {
                gameClient.requestAction(sendableEndTurnEvent);
            }
        }
        // TODO: Acts as a temporary way to end the other players turn until the game initialization and test setup has been cleanuped
        else if ("delete".equals(name) && isPressed) {
            int activePlayerEntity = gameClient.getGame().getData().entity(Components.ACTIVE_PLAYER);
            gameClient.requestAction(new EndTurnEvent(activePlayerEntity));
        }
    }

    private void initBoard() {
        board = new Board<>(new DebugZoneVisualizer() {

            @Override
            protected Vector2f getSize(CardZone zone) {
                for (PlayerZones playerZones : playerZonesMap.values()) {
                    if (zone == playerZones.getDeckZone()) {
                        return new Vector2f(1.2f, 1.2f);
                    } else if (zone == playerZones.getHandZone()) {
                        return new Vector2f(6, 2);
                    } else if (zone == playerZones.getBoardZone()) {
                        return new Vector2f(10, 3);
                    }
                }
                return super.getSize(zone);
            }
        }, new SimpleCardVisualizer<CardModel>() {

            @Override
            public PaintableImage paintCard(CardModel cardModel) {
                PaintableImage paintableImage = new PaintableImage(400, 560);
                CardPainterJME.drawCard(paintableImage, cardModel);
                return paintableImage;
            }
        });
        updateBoardService = new UpdateBoardService(gameClient, board, playerZonesMap, cardGuiMap);
        initGameListeners();
    }

    private void initGameListeners() {
        gameClient.addFullGameStateListener(entityData -> {
            IntArrayList players = entityData.entities(Components.NEXT_PLAYER);
            Vector3f offset = new Vector3f(0, 0, 2);
            for (int i = 0; i < players.size(); i++) {
                if (i == 1) {
                    offset.addLocal(0, 0, -6);
                }
                IntervalZone deckZone = new IntervalZone(new Vector3f(0.04f, 0, 0)) {

                    // TODO: Cleanup
                    @Override
                    public Vector3f getLocalPosition(Vector3f zonePosition) {
                        Vector3f localPosition = super.getLocalPosition(zonePosition);
                        return new Vector3f(localPosition.y, localPosition.x, localPosition.z);
                    }
                };
                IntervalZone handZone = new IntervalZone(new Vector3f(1, 1, 1));
                IntervalZone boardZone = new IntervalZone(new Vector3f(1, 1, 1));
                boardZone.setPositionTransformation(new SimpleTargetPositionTransformation(offset.add(0, 0, 0)));
                handZone.setPositionTransformation(new SimpleTargetPositionTransformation(offset.add(0, 0, 2.5f)));
                deckZone.setPositionTransformation(new SimpleTargetPositionTransformation(offset.add(10, 0, 0)));
                handZone.setRotationTransformation(new SimpleTargetRotationTransformation(new Quaternion().fromAngleAxis(FastMath.QUARTER_PI, Vector3f.UNIT_X)));
                board.addZone(deckZone);
                board.addZone(handZone);
                board.addZone(boardZone);
                playerZonesMap.put(players.get(i), new PlayerZones(deckZone, handZone, boardZone));
            }
            BoardAppState boardAppState = new BoardAppState<>(board, mainApplication.getRootNode());
            boardAppState.setDraggedCardProjectionZ(0.9975f);
            mainApplication.getStateManager().attach(boardAppState);
            updateAndResetBoard();
            updatePossibleActions();
            hasPreparedBoard = true;
        });
        gameClient.getGame().getPostDispatcher().addListeners(Event.class, event -> updateAndResetBoard());
        gameClient.getGame().getPreDispatcher().addListeners(DamageEvent.class, event -> board.playAnimation(new CameraShakeAnimation(mainApplication.getCamera(), 1, 0.01f)));
        gameClient.getGame().getPreDispatcher().addListeners(ShuffleLibraryEvent.class, event -> {
            LinkedList<Card> deckCards = playerZonesMap.get(event.player).getDeckZone().getCards();
            //board.playAnimation(new ShuffleAnimation(deckCards, mainApplication));
        });
    }

    private void updateAndResetBoard() {
        mainApplication.enqueue(() -> {
            updateBoardService.updateAndResetInteractivities();
            sendableEndTurnEvent = null;
        });
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if ((!isInitialized) && hasPreparedBoard) {
            gameClient.markAsReady();
            isInitialized = true;
        }
        processNextEvents();
    }

    private void processNextEvents() {
        if (gameClient.getGame().getEvents().hasNext()) {
            while (!board.isAnimationQueueBlocking()) {
                gameClient.getGame().getEvents().processNextEvent();
                if (!gameClient.getGame().getEvents().hasNext()) {
                    updatePossibleActions();
                    break;
                }
            }
        }
    }

    private void updatePossibleActions() {
        mainApplication.enqueue(() -> {
            List<Event> possibleEvents = PlayerActionsGenerator.generatePossibleActions(gameClient.getGame().getData(), gameClient.getPlayerEntity());
            updateBoardService.updateInteractivities(possibleEvents);
            for (Event event : possibleEvents) {
                if (event instanceof EndTurnEvent) {
                    sendableEndTurnEvent = (EndTurnEvent) event;
                }
            }
        });
    }
}
