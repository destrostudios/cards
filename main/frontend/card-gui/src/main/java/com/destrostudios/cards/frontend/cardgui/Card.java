package com.destrostudios.cards.frontend.cardgui;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public class Card extends TransformedBoardObject{

    private ZonePosition zonePosition = new ZonePosition();

    public ZonePosition getZonePosition() {
        return zonePosition;
    }

    @Override
    protected Vector3f getDefaultTargetPosition() {
        return zonePosition.getDefaultTargetPosition();
    }

    @Override
    protected Quaternion getDefaultTargetRotation() {
        return zonePosition.getDefaultTargetRotation();
    }
}
