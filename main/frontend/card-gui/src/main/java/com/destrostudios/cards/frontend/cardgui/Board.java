package com.destrostudios.cards.frontend.cardgui;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Carl
 */
public class Board implements GameLoopListener {

    public Board(BoardObjectVisualizer<CardZone> zoneVisualizer, BoardObjectVisualizer<Card> cardVisualizer, InteractivityListener interactivityListener) {
        this.zoneVisualizer = zoneVisualizer;
        this.cardVisualizer = cardVisualizer;
        this.interactivityListener = interactivityListener;
    }
    private int nextId;
    private HashMap<Integer, BoardObject> boardObjects = new HashMap<Integer, BoardObject>();
    private LinkedList<CardZone> zones = new LinkedList<CardZone>();
    private BoardObjectVisualizer<CardZone> zoneVisualizer;
    private BoardObjectVisualizer<Card> cardVisualizer;
    private InteractivityListener interactivityListener;
    private AnimationQueue animationQueue = new AnimationQueue();
    
    public void register(BoardObject boardObject) {
        if (boardObject.getId() == -1) {
            boardObject.setId(nextId);
            boardObjects.put(nextId, boardObject);
            nextId++;
        }
    }

    public Collection<BoardObject> getBoardObjects() {
        return boardObjects.values();
    }

    public BoardObject getBoardObject(Integer id) {
        return boardObjects.get(id);
    }
    
    public void addZone(CardZone zone) {
        register(zone);
        zone.setBoard(this);
        zones.add(zone);
    }

    public void triggerEvent(GameEvent event) {
        event.trigger(this);
    }

    public void playAnimation(Animation animation) {
        animationQueue.addAnimation(animation);
    }

    @Override
    public void update(float lastTimePerFrame) {
        for (BoardObject boardObject : boardObjects.values()) {
            boardObject.update(lastTimePerFrame);
        }
        animationQueue.update(lastTimePerFrame);
    }

    public LinkedList<CardZone> getZones() {
        return zones;
    }

    public BoardObjectVisualizer getVisualizer(BoardObject boardObject) {
        if (boardObject instanceof Card) {
            return cardVisualizer;
        }
        else if (boardObject instanceof CardZone) {
            return zoneVisualizer;
        }
        return null;
    }

    public InteractivityListener getInteractivityListener() {
        return interactivityListener;
    }
}
