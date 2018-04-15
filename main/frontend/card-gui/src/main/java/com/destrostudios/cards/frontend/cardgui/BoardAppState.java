package com.destrostudios.cards.frontend.cardgui;

import com.destrostudios.cards.frontend.cardgui.interactivities.AimToTargetInteractivity;
import com.destrostudios.cards.frontend.cardgui.targetarrow.TargetArrow;
import com.destrostudios.cards.frontend.cardgui.targetarrow.TargetSnapMode;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Carl
 */
public class BoardAppState extends BaseAppState implements ActionListener{

    public BoardAppState(Board board, Node rootNode) {
        this.board = board;
        this.rootNode = rootNode;
    }
    private Board board;
    private Node rootNode;
    private Application application;
    private RayCasting rayCasting;
    private HashMap<BoardObject, Node> boardObjectNodes = new HashMap<BoardObject, Node>();
    private BoardObject draggedBoardObject;
    private Node draggedNode;
    private BoardObjectFilter nonDraggedNodeFilter = new BoardObjectFilter() {
        
        @Override
        public boolean isValid(BoardObject boardObject) {
            return (boardObject != draggedBoardObject);
        }
    };
    private TargetArrow targetArrow;

    @Override
    protected void initialize(Application app) {
        application = app;
        rayCasting = new RayCasting(application);
        for (CardZone zone : board.getZones()) {
            Node zoneNode = new Node();
            zoneNode.setUserData("boardObjectId", zone.getId());
            board.getVisualizer(zone).createVisualisation(zoneNode, application.getAssetManager());
            board.getVisualizer(zone).updateVisualisation(zoneNode, zone, application.getAssetManager());
            rootNode.attachChild(zoneNode);
            boardObjectNodes.put(zone, zoneNode);
            for (Card card : zone.getCards()) {
                card.setProperty("mana", "0");
                Node cardNode = new Node();
                cardNode.setUserData("boardObjectId", card.getId());
                board.getVisualizer(card).createVisualisation(cardNode, application.getAssetManager());
                board.getVisualizer(card).updateVisualisation(cardNode, card, application.getAssetManager());
                rootNode.attachChild(cardNode);
                boardObjectNodes.put(card, cardNode);
            }
        }
        targetArrow = new TargetArrow(application.getAssetManager());
        
        application.getInputManager().addMapping("mouse_click_left", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        application.getInputManager().addMapping("mouse_click_middle", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        application.getInputManager().addMapping("mouse_click_right", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        application.getInputManager().addListener(this, new String[]{
            "mouse_click_left","mouse_click_middle","mouse_click_right"
        });
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        for (Map.Entry<BoardObject, Node> boardObjectEntry : boardObjectNodes.entrySet()) {
            BoardObject boardObject = boardObjectEntry.getKey();
            Node node = boardObjectEntry.getValue();
            if (boardObject == draggedBoardObject) {
                continue;
            }
            
            if (boardObject.needsVisualisationUpdate()) {
                board.getVisualizer(boardObject).updateVisualisation(node, boardObject, application.getAssetManager());
                boardObject.onVisualisationUpdate();
            }
            if (boardObject instanceof Card) {
                Card card = (Card) boardObject;
                CardZone zone = card.getZonePosition().getZone();
                Node zoneNode = boardObjectNodes.get(zone);

                Vector3f worldPosition = zone.getWorldPosition(card.getZonePosition().getPosition());
                worldPosition = worldPosition.add(zoneNode.getLocalTranslation());

                node.setLocalTranslation(FloatInterpolate.get(node.getLocalTranslation(), worldPosition, lastTimePerFrame));
                node.setLocalRotation(FloatInterpolate.get(node.getLocalRotation(), zoneNode.getLocalRotation(), lastTimePerFrame));
                //node.setLocalScale(FloatInterpolate.get(node.getLocalScale(), zoneNode.getLocalScale(), lastTimePerFrame));
            }
        }
        if (draggedNode != null) {
            Vector2f cursorPosition = application.getInputManager().getCursorPosition();
            Vector3f cursorWorldLocation = application.getCamera().getWorldCoordinates(cursorPosition, 0.8f);

            Interactivity interactivity = draggedBoardObject.getInteractivity();
            switch (interactivity.getType()) {
                case DRAG:
                    draggedNode.setLocalTranslation(cursorWorldLocation);
                    // Set rotation so the node faces the camera (2d-like)
                    draggedNode.setLocalRotation(application.getCamera().getRotation());
                    draggedNode.rotate(-FastMath.HALF_PI, 0, FastMath.PI);
                    break;
                
                case AIM:
                    Vector3f sourceLocation = draggedNode.getLocalTranslation();
                    Vector3f targetLocation = cursorWorldLocation;
                    AimToTargetInteractivity dragToTargetInteractivity = (AimToTargetInteractivity) interactivity;
                    if (dragToTargetInteractivity.getTargetSnapMode() != TargetSnapMode.NEVER) {
                        BoardObject hoveredBoardObject = getHoveredInteractivityTarget(dragToTargetInteractivity.getTargetSnapMode() == TargetSnapMode.VALID);
                        if (hoveredBoardObject != null) {
                            targetLocation = getBoardObjectNode(hoveredBoardObject).getLocalTranslation();
                        }
                    }
                    targetArrow.updateGeometry(sourceLocation, targetLocation);
                    break;
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float lastTimePerFrame) {
        switch(name) {
            case "mouse_click_left":
                if (isPressed) {
                    BoardObject boardObject = getHoveredBoardObject(BoardObjectFilter.CARD);
                    if (boardObject == null) {
                        boardObject = getHoveredBoardObject(BoardObjectFilter.ZONE);
                    }
                    if (boardObject != null) {
                        Interactivity interactivity = boardObject.getInteractivity();
                        if (interactivity != null) {
                            switch (interactivity.getType()) {
                                case CLICK:
                                    board.getInteractivityListener().onInteractivity(boardObject, null);
                                    break;
                                case DRAG:
                                    draggedBoardObject = boardObject;
                                    draggedNode = boardObjectNodes.get(boardObject);
                                    break;
                                case AIM:
                                    draggedBoardObject = boardObject;
                                    draggedNode = boardObjectNodes.get(boardObject);
                                    rootNode.attachChild(targetArrow.getGeometry());
                                    break;
                            }
                        }
                    }
                }
                else {
                    if (draggedBoardObject != null) {
                        switch (draggedBoardObject.getInteractivity().getType()) {
                            case DRAG:
                                board.getInteractivityListener().onInteractivity(draggedBoardObject, null);
                                break;

                            case AIM:
                                BoardObject hoveredBoardObject = getHoveredInteractivityTarget(true);
                                if (hoveredBoardObject != null) {
                                    board.getInteractivityListener().onInteractivity(draggedBoardObject, hoveredBoardObject);
                                }
                                break;
                        }
                        draggedNode = null;
                        draggedBoardObject = null;
                        rootNode.detachChild(targetArrow.getGeometry());
                    }
                }
                break;
        }
    }
    
    private BoardObject getHoveredInteractivityTarget(boolean filterValidTargets) {
        BoardObjectFilter targetFilter = nonDraggedNodeFilter;
        if (filterValidTargets && (draggedBoardObject.getInteractivity() instanceof BoardObjectFilter)) {
            BoardObjectFilter interactivityFilter = (BoardObjectFilter) draggedBoardObject.getInteractivity();
            targetFilter = BoardObjectFilter.getCompositeFilter(targetFilter, interactivityFilter);
        }
        return getHoveredBoardObject(targetFilter);
    }

    private BoardObject getHoveredBoardObject(BoardObjectFilter filter) {
        CollisionResults collisionResults = rayCasting.getResults_Cursor(rootNode);
        for (int i=0;i<collisionResults.size();i++) {
            CollisionResult collisionResult = collisionResults.getCollision(i);
            Spatial spatial = collisionResult.getGeometry();
            while (spatial.getParent() != null) {
                spatial = spatial.getParent();
                Integer cardId = spatial.getUserData("boardObjectId");
                if (cardId != null) {
                    BoardObject boardObject = board.getBoardObject(cardId);
                    if ((filter == null) || filter.isValid(boardObject)) {
                        return boardObject;
                    }
                }
            }
        }
        return null;
    }

    public Board getBoard() {
        return board;
    }

    protected Node getBoardObjectNode(BoardObject boardObject) {
        return boardObjectNodes.get(boardObject);
    }
    
    // TODO: Other appstate interface methods

    @Override
    protected void cleanup(Application app) {
        
    }

    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {
        
    }
}
