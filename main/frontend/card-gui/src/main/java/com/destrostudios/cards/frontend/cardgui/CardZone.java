package com.destrostudios.cards.frontend.cardgui;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.LinkedList;

/**
 *
 * @author Carl
 */
public abstract class CardZone extends TransformedBoardObject {

    private Board board;
    private LinkedList<Card> cards = new LinkedList<>();
    
    public void addCard(Card card, Vector3f position) {
        board.register(card);
        ZonePosition zonePosition = card.getZonePosition();
        if (zonePosition.getZone() != null) {
            zonePosition.getZone().removeCard(card);
        }
        card.getZonePosition().setZone(this);
        card.getZonePosition().setPosition(position);
        cards.add(card);
    }

    public void removeCard(Card card) {
        card.getZonePosition().setZone(null);
        card.getZonePosition().setPosition(null);
        cards.remove(card);
    }
    
    public abstract Vector3f getLocalPosition(Vector3f zonePosition);

    public void setBoard(Board board) {
        this.board = board;
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    @Override
    protected Vector3f getDefaultTargetPosition() {
        return Vector3f.ZERO;
    }

    @Override
    protected Quaternion getDefaultTargetRotation() {
        return Quaternion.DIRECTION_Z;
    }
}
