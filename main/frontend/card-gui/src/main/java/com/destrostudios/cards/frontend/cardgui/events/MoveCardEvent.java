package com.destrostudios.cards.frontend.cardgui.events;

import com.destrostudios.cards.frontend.cardgui.*;
import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public class MoveCardEvent extends GameEvent{

    public MoveCardEvent(Card card, CardZone zone, Vector3f position) {
        this.card = card;
        this.zone = zone;
        this.position = position;
    }
    private Card card;
    private CardZone zone;
    private Vector3f position;

    @Override
    public void trigger(Board board) {
        zone.addCard(card, position);
    }
}
