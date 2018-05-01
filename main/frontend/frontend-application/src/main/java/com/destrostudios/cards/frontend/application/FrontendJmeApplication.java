package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.*;
import com.destrostudios.cards.frontend.cardgui.events.*;
import com.destrostudios.cards.frontend.cardgui.files.FileAssets;
import com.destrostudios.cards.frontend.cardgui.transformations.*;
import com.destrostudios.cards.frontend.cardgui.visualisation.*;
import com.destrostudios.cards.frontend.cardgui.zones.IntervalZone;
import com.destrostudios.cards.frontend.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.*;
import com.jme3.system.AppSettings;

import java.util.HashMap;

public class FrontendJmeApplication extends SimpleApplication implements ActionListener {

    public FrontendJmeApplication(SimpleGameClient gameClient) {
        this.gameClient = gameClient;
        loadSettings();
    }
    private SimpleGameClient gameClient;
    private Board<CardModel> board;
    private HashMap<Integer, PlayerZones> playerZonesMap = new HashMap<>();
    private CardGuiMap cardGuiMap = new CardGuiMap();

    private void loadSettings(){
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cards");
        setShowSettings(false);
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator(FileAssets.ROOT, FileLocator.class);

        initCamera();
        initListeners();
        initBoard();
        
        gameClient.start();
    }

    private void initCamera() {
        flyCam.setMoveSpeed(100);
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(2.584369f, 14.878008f, 0.86850137f));
        cam.setRotation(new Quaternion(-0.001344382f, 0.72532254f, -0.6884064f, -0.0014168395f));
    }

    private void initListeners() {
        inputManager.addMapping("space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "space");
    }

    @Override
    public void onAction(String name, boolean isPressed, float lastTimePerFrame) {
        if ("space".equals(name) && isPressed) {
            inputManager.setCursorVisible(flyCam.isEnabled());
            flyCam.setEnabled(!flyCam.isEnabled());
        }
    }

    private void initBoard() {
        board = new Board<>(new DebugZoneVisualizer() {

            @Override
            protected Vector2f getSize(CardZone zone) {
                for (PlayerZones playerZones : playerZonesMap.values()) {
                    if (zone == playerZones.getDeckZone()) {
                        return new Vector2f(1.2f, 1.2f);
                    }
                    else if (zone == playerZones.getHandZone()) {
                        return new Vector2f(6, 2);
                    }
                    else if (zone == playerZones.getBoardZone()) {
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
        }, new InteractivityListener() {

            @Override
            public void onInteractivity(BoardObject boardObject, BoardObject target) {
                System.out.println(boardObject + "\t" + target);
            }
        });

        gameClient.getGame().getDispatcher().addListeners(GameStartEvent.class, event -> {
            IntArrayList players = gameClient.getGame().getData().entities(Components.NEXT_PLAYER);
            Vector3f offset = new Vector3f(0, 0, 2);
            for (int i = 0; i < players.size(); i++) {
                if (i == 1) {
                    offset.addLocal(0, 0, -6);
                }
                IntervalZone deckZone = new IntervalZone(new Vector3f(0, 0.04f, 0));
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
            stateManager.attach(new BoardAppState<>(board, rootNode));

            IntArrayList cardEntities = gameClient.getGame().getData().entities(Components.OWNED_BY);
            for (int cardEntity : cardEntities) {
                Card<CardModel> card = cardGuiMap.getOrCreateCard(cardEntity);
                int cardZoneIndex = getCardZoneIndex(cardEntity);
                if (cardZoneIndex != -1) {
                    CardGuiMapper.updateModel(card, gameClient.getGame().getData(), cardEntity);
                    board.triggerEvent(new ModelUpdatedEvent(card));
                    board.triggerEvent(new MoveCardEvent(card, getCardZone(cardEntity), new Vector3f(cardZoneIndex, 0, 0)));
                }
            }
        });
    }

    private int getCardZoneIndex(int cardEntity) {
        if (gameClient.getGame().getData().has(cardEntity, Components.LIBRARY)) {
            return gameClient.getGame().getData().get(cardEntity, Components.LIBRARY);
        }
        else if (gameClient.getGame().getData().has(cardEntity, Components.HAND_CARDS)) {
            return gameClient.getGame().getData().get(cardEntity, Components.HAND_CARDS);
        }
        else if (gameClient.getGame().getData().has(cardEntity, Components.CREATURE_ZONE)) {
            return gameClient.getGame().getData().get(cardEntity, Components.CREATURE_ZONE);
        }
        return -1;
    }

    private CardZone getCardZone(int cardEntity) {
        int playerEntity = gameClient.getGame().getData().get(cardEntity, Components.OWNED_BY);
        PlayerZones playerZones = playerZonesMap.get(playerEntity);
        if (gameClient.getGame().getData().has(cardEntity, Components.LIBRARY)) {
            return playerZones.getDeckZone();
        }
        else if (gameClient.getGame().getData().has(cardEntity, Components.HAND_CARDS)) {
            return playerZones.getHandZone();
        }
        else if (gameClient.getGame().getData().has(cardEntity, Components.CREATURE_ZONE)) {
            return playerZones.getBoardZone();
        }
        return null;
    }
}