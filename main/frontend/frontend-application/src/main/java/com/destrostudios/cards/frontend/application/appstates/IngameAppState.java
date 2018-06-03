package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.shared.rules.game.phases.main.EndMainPhaseOneEvent;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.attack.EndAttackPhaseEvent;
import com.destrostudios.cards.frontend.application.*;
import com.destrostudios.cards.frontend.application.appstates.services.UpdateBoardService;
import com.destrostudios.cards.frontend.cardgui.*;
import com.destrostudios.cards.frontend.cardgui.animations.samples.*;
import com.destrostudios.cards.frontend.cardgui.visualisation.*;
import com.destrostudios.cards.frontend.cardgui.zones.*;
import com.destrostudios.cards.frontend.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.game.phases.main.EndMainPhaseTwoEvent;
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
        cardGuiMap = new CardGuiMap(gameClient.getGame().getData());
    }

    private static final float ZONE_HEIGHT = 1.3f;
    private SimpleGameClient gameClient;
    private Board<CardModel> board;
    private HashMap<Integer, PlayerZones> playerZonesMap = new HashMap<>();
    private CardGuiMap cardGuiMap;
    private boolean hasPreparedBoard = false;
    private boolean isInitialized = false;
    private UpdateBoardService updateBoardService;
    // TODO: Cleanup, solve better
    private Event sendableEndTurnEvent;

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
        camera.setFrustumPerspective(45, (float) camera.getWidth() / camera.getHeight(), 0.01f, 1000);
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
        } else if ("end".equals(name) && isPressed) {
            if (sendableEndTurnEvent != null) {
                gameClient.requestAction(sendableEndTurnEvent);
            }
        } // TODO: Acts as a temporary way to end the other players turn until the game initialization and test setup has been cleanuped
        else if ("delete".equals(name) && isPressed) {
            int activePlayerEntity = gameClient.getGame().getData().query(Components.Game.TURN_PHASE).unique().getAsInt();
            switch (gameClient.getGame().getData().getComponent(activePlayerEntity, Components.Game.TURN_PHASE)) {
                case MAIN_ONE:
                    gameClient.requestAction(new EndMainPhaseOneEvent(activePlayerEntity));
                    break;
                case ATTACK:
                    gameClient.requestAction(new EndAttackPhaseEvent(activePlayerEntity));
                    break;
                case BLOCK:
                    gameClient.requestAction(new EndBlockPhaseEvent(activePlayerEntity));
                    break;
                case MAIN_TWO:
                    gameClient.requestAction(new EndMainPhaseTwoEvent(activePlayerEntity));
                    break;
                default:
                    throw new AssertionError(gameClient.getGame().getData().getComponent(activePlayerEntity, Components.Game.TURN_PHASE).name());
            }
        }
    }

    private void initBoard() {
        board = new Board<>(new DebugZoneVisualizer() {

            @Override
            protected Vector2f getSize(CardZone zone) {
                for (PlayerZones playerZones : playerZonesMap.values()) {
                    if (zone == playerZones.getDeckZone()) {
                        return new Vector2f(1, ZONE_HEIGHT);
                    } else if (zone == playerZones.getHandZone()) {
                        return new Vector2f(5, ZONE_HEIGHT - 0.1f);
                    } else if (zone == playerZones.getLandZone()) {
                        return new Vector2f(6.5f, ZONE_HEIGHT);
                    } else if (zone == playerZones.getCreatureZone()) {
                        return new Vector2f(5, ZONE_HEIGHT);
                    } else if (zone == playerZones.getEnchantmentZone()) {
                        return new Vector2f(1.5f, ZONE_HEIGHT);
                    } else if (zone == playerZones.getGraveyardZone()) {
                        return new Vector2f(1, ZONE_HEIGHT);
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
            List<Integer> players = entityData.query(Components.NEXT_PLAYER).list();
            Vector3f offset = new Vector3f(0, 0, ZONE_HEIGHT);
            float directionX = 1;
            float directionZ = 1;
            Quaternion zoneRotation = Quaternion.IDENTITY;
            for (int i = 0; i < players.size(); i++) {
                if (i == 1) {
                    offset.addLocal(3.5f, 0, 0);
                    directionX *= -1;
                    directionZ *= -1;
                    zoneRotation = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);
                }

                float x = 0.5f;
                float z = (ZONE_HEIGHT / 2);
                CenteredIntervalZone creatureZone = new CenteredIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(1, 1, 1));
                x += 3.25f;
                CenteredIntervalZone enchantmentZone = new CenteredIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(1, 1, 1));
                x += 1.25f;
                SimpleIntervalZone graveyardZone = new SimpleIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(0.04f, 1, 1)) {

                    // TODO: Cleanup
                    @Override
                    public Vector3f getLocalPosition(Vector3f zonePosition) {
                        Vector3f localPosition = super.getLocalPosition(zonePosition);
                        return new Vector3f(localPosition.y, localPosition.x, localPosition.z);
                    }
                };

                x = 1.25f;
                z += ZONE_HEIGHT;
                CenteredIntervalZone landZone = new CenteredIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(1, 1, 1));
                x += 3.75f;
                SimpleIntervalZone deckZone = new SimpleIntervalZone(offset.add(directionX * x, 0, directionZ * z), zoneRotation, new Vector3f(0.04f, 0, 0)) {

                    // TODO: Cleanup
                    @Override
                    public Vector3f getLocalPosition(Vector3f zonePosition) {
                        Vector3f localPosition = super.getLocalPosition(zonePosition);
                        return new Vector3f(localPosition.y, localPosition.x, localPosition.z);
                    }
                };

                x = 1.5f;
                z += (ZONE_HEIGHT - 0.25f);
                Quaternion handRotation = zoneRotation.mult(new Quaternion().fromAngleAxis(FastMath.QUARTER_PI, Vector3f.UNIT_X));
                CenteredIntervalZone handZone = new CenteredIntervalZone(offset.add(directionX * x, 0, directionZ * z), handRotation, new Vector3f(0.85f, 1, 1));

                board.addZone(deckZone);
                board.addZone(handZone);
                board.addZone(landZone);
                board.addZone(creatureZone);
                board.addZone(enchantmentZone);
                board.addZone(graveyardZone);
                playerZonesMap.put(players.get(i), new PlayerZones(deckZone, handZone, landZone, creatureZone, enchantmentZone, graveyardZone));
            }
            BoardAppState boardAppState = new BoardAppState<>(board, mainApplication.getRootNode());
            boardAppState.setDraggedCardProjectionZ(0.9975f);
            mainApplication.getStateManager().attach(boardAppState);
            updateAndResetBoard();
            updatePossibleActions();
            mainApplication.enqueue(() -> {
                board.finishAllTransformations();
            });
            hasPreparedBoard = true;

            Camera camera = mainApplication.getCamera();
            camera.setLocation(new Vector3f(1.8497083f, 3.8661501f, 6.470482f));
            camera.lookAtDirection(new Vector3f(0, -0.7237764f, -0.6900346f), Vector3f.UNIT_Y);
            // TODO: Maybe check this other than the exact hardcoded entity
            if (gameClient.getPlayerEntity() == 1) {
                camera.setLocation(camera.getLocation().add(0, 0, -10.3f));
                camera.setRotation(new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y).multLocal(camera.getRotation()));
            }
        });
        gameClient.getGame().getPostDispatcher().addListeners(Event.class, event -> updateAndResetBoard());
        gameClient.getGame().getPreDispatcher().addListeners(BattleEvent.class, event -> board.playAnimation(new CameraShakeAnimation(mainApplication.getCamera(), 1, 0.01f)));
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
            List<Event> possibleEvents = gameClient.getGame().getActionGenerator().generatePossibleActions(gameClient.getPlayerEntity());
            updateBoardService.updateInteractivities(possibleEvents);
            for (Event event : possibleEvents) {
                if (event instanceof EndAttackPhaseEvent || event instanceof EndBlockPhaseEvent || event instanceof EndMainPhaseOneEvent) {
                    sendableEndTurnEvent = event;
                }
            }
        });
    }
}
