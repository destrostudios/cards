package com.destrostudios.cards.frontend.application.appstates.services.zones;

import com.destrostudios.cardgui.CardZone;
import com.destrostudios.cards.frontend.application.appstates.services.DeckBuilderCardVisualizer;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class DeckBuilderDeckZone extends CardZone {

    public DeckBuilderDeckZone(Vector3f position) {
        super(position, new Quaternion());
    }

    protected Vector3f getLocalCardPosition(Vector3f zonePosition) {
        Vector3f cardPosition = zonePosition.clone();
        cardPosition.z *= (getCardScaleZ() * DeckBuilderCardVisualizer.GEOMETRY_HEIGHT);
        return cardPosition;
    }

    @Override
    protected Vector3f getLocalCardScale(Vector3f zonePosition) {
        return new Vector3f(1, 1, getCardScaleZ());
    }

    private float getCardScaleZ() {
        return Math.min(16f / cards.size(), 1);
    }
}
