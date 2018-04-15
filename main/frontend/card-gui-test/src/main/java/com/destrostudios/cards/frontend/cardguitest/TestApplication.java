package com.destrostudios.cards.frontend.cardguitest;

import com.destrostudios.cards.frontend.cardgui.*;
import com.destrostudios.cards.frontend.cardgui.events.*;
import com.destrostudios.cards.frontend.cardgui.files.FileAssets;
import com.destrostudios.cards.frontend.cardgui.interactivities.*;
import com.destrostudios.cards.frontend.cardgui.targetarrow.TargetSnapMode;
import com.destrostudios.cards.frontend.cardgui.visualisation.*;
import com.destrostudios.cards.frontend.cardgui.zones.*;
import com.destrostudios.cards.frontend.cardguitest.game.*;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import java.util.HashMap;
import java.util.Iterator;

public class TestApplication extends SimpleApplication implements ActionListener{

    public static void main(String[] args) {
        FileAssets.readRootFile();
        
        TestApplication app = new TestApplication();
        app.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("CardGui - TestApp");
        app.setSettings(settings);
        app.start();
    }
    private Board board;
    private MyGame game;
    private PlayerZones[] playerZones;
    private HashMap<MyCard, Card> visualCards = new HashMap<>();
    private HashMap<Card, MyCard> gameCards = new HashMap<>();

    @Override
    public void simpleInitApp() {
        initCamera();
        initListeners();
        initGame();
        initBoardGui();

        game.start();
        updateBoard();
    }

    @Override
    public void simpleUpdate(float lastTimePerFrame) {
        // Nothing needs to be done here
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
    
    private void initGame() {
        MyPlayer[] players = new MyPlayer[]{new MyPlayer(), new MyPlayer()};
        for (MyPlayer player : players) {
            for (int i = 0; i < 30; i++) {
                MyCard.Color color = MyCard.Color.values()[(int) (Math.random() * MyCard.Color.values().length)];
                String name = ((Math.random() < 0.5) ? "Aether Adept" : "Shyvana");
                player.getDeck().addCard(new MyCard(color, name));
            }
        }
        game = new MyGame(players);
    }
    
    private void initBoardGui() {
        board = new Board(new DebugZoneVisualizer() {

            @Override
            protected Vector2f getSize(CardZone zone) {
            for (int i=0;i<game.getPlayers().length;i++) {
                if (zone == playerZones[i].getDeckZone()) {
                    return new Vector2f(1.2f, 1.2f);
                }
                else if (zone == playerZones[i].getHandZone()) {
                    return new Vector2f(6, 2);
                }
                else if (zone == playerZones[i].getBoardZone()) {
                    return new Vector2f(10, 3);
                }
            }
            return super.getSize(zone);
            }
        }, new SimpleCardVisualizer() {
            
            @Override
            public PaintableImage paintCard(Card card) {
            PaintableImage paintableImage = new PaintableImage(300, 400);
            paintableImage.setBackground_Alpha(0);
            paintableImage.paintImage(new PaintableImage("images/cards/" + card.getProperty("name") + ".png"), 74, 43, 155, 155);
            paintableImage.paintImage(new PaintableImage("images/templates/mana_" + card.getProperty("color") + ".png"), 0, 0, 300, 400);
            if ("true".equals(card.getProperty("damaged"))) {
                for (int x=0;x<paintableImage.getWidth();x++) {
                    for (int y=0;y<paintableImage.getHeight();y++) {
                        paintableImage.setPixel_Red(x, y, 255);
                    }
                }
            }
            return paintableImage;
            }
        }, new InteractivityListener() {

            @Override
            public void onInteractivity(BoardObject boardObject, BoardObject target) {
                if (boardObject instanceof Card) {
                    Card card = (Card) boardObject;
                    MyCard myCard = gameCards.get(card);
                    if (card.getInteractivity() instanceof ClickInteractivity) {
                        game.getCurrentPlayer().draw();
                        updateBoard();
                    }
                    else if (card.getInteractivity() instanceof DragToPlayInteractivity) {
                        for (MyPlayer player : game.getPlayers()) {
                            if (player.getHand().contains(myCard)) {
                                player.getHand().removeCard(myCard);
                                player.getBoard().addCard(myCard);
                                updateBoard();
                                break;
                            }
                        }
                    }
                    else if (card.getInteractivity() instanceof AimToTargetInteractivity) {
                        Card targetCard = (Card) target;
                        MyCard targetMyCard = gameCards.get(targetCard);
                        myCard.setDamaged(true);
                        targetMyCard.setDamaged(true);
                        updateBoard();
                    }
                }
            }
        });
        
        int playersCount = game.getPlayers().length;
        playerZones = new PlayerZones[playersCount];
        for (int i=0;i<playersCount;i++) {
            IntervalZone deckZone = new IntervalZone(new Vector3f(0, 0.04f, 0));
            IntervalZone handZone = new IntervalZone(new Vector3f(1, 1, 1));
            IntervalZone boardZone = new IntervalZone(new Vector3f(1, 1, 1));
            board.addZone(deckZone);
            board.addZone(handZone);
            board.addZone(boardZone);
            playerZones[i] = new PlayerZones(deckZone, handZone, boardZone);
        }

        stateManager.attach(new BoardAppState(board, rootNode){

            @Override
            protected void initialize(Application app) {
                super.initialize(app);
                Vector3f offset = new Vector3f(0, 0, 2);
                for (int i=0;i<game.getPlayers().length;i++) {
                    if (i == 1) {
                        offset.addLocal(0, 0, -6);
                    }
                    PlayerZones zones = playerZones[i];
                    getBoardObjectNode(zones.getBoardZone()).setLocalTranslation(offset.add(0, 0, 0));
                    getBoardObjectNode(zones.getHandZone()).setLocalTranslation(offset.add(0, 0, 2.5f));
                    getBoardObjectNode(zones.getDeckZone()).setLocalTranslation(offset.add(10, 0, 0));
                    getBoardObjectNode(zones.getHandZone()).rotate(new Quaternion().fromAngleAxis(FastMath.PI * 0.25f, Vector3f.UNIT_X));
                }
            }
        });
    }
    
    private void updateBoard() {
        for (int i=0;i<game.getPlayers().length;i++) {
            final int opponentPlayerIndex = ((i + 1) % 2);
            MyPlayer player = game.getPlayers()[i];
            updateZone(player.getDeck(), playerZones[i].getDeckZone(), new Vector3f(0, 1, 0), new ClickInteractivity());
            updateZone(player.getHand(), playerZones[i].getHandZone(), new Vector3f(1, 0, 0), new DragToPlayInteractivity());
            updateZone(player.getBoard(), playerZones[i].getBoardZone(), new Vector3f(1, 0, 0), new AimToTargetInteractivity(TargetSnapMode.VALID){

                @Override
                public boolean isValid(BoardObject boardObject) {
                    if (boardObject instanceof Card) {
                        Card card = (Card) boardObject;
                        if (card.getZonePosition().getZone() == playerZones[opponentPlayerIndex].getBoardZone()) {
                            return !"0".equals(card.getProperty("color"));
                        }
                    }
                    return false;
                }
            });
        }
    }
    
    private void updateZone(MyCards myCards, CardZone cardZone, Vector3f interval, Interactivity interactivity) {
        int index = 0;
        Iterator<MyCard> deckIterator = myCards.iterator();
        while (deckIterator.hasNext()) {
            MyCard myCard = deckIterator.next();
            Card card = getOrCreateCard(myCard);
            board.triggerEvent(new SetPropertyEvent(card, "color", "" + myCard.getColor().ordinal()));
            board.triggerEvent(new SetPropertyEvent(card, "name", myCard.getName()));
            board.triggerEvent(new SetPropertyEvent(card, "damaged", Boolean.toString(myCard.isDamaged())));
            board.triggerEvent(new MoveCardEvent(card, cardZone, interval.mult(index)));
            card.setInteractivity(interactivity);
            index++;
        }
    }
    
    private Card getOrCreateCard(MyCard myCard) {
        Card card = visualCards.get(myCard);
        if (card == null) {
            card = new Card();
            visualCards.put(myCard, card);
            gameCards.put(card, myCard);
        }
        return card;
    }

    @Override
    public void onAction(String name, boolean isPressed, float lastTimePerFrame) {
        if ("space".equals(name) && isPressed) {
            inputManager.setCursorVisible(flyCam.isEnabled());
            flyCam.setEnabled(!flyCam.isEnabled());
        }
    }
}
