package com.destrostudios.cards.frontend.cardgui.zones;

import com.destrostudios.cards.frontend.cardgui.*;
import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public class IntervalZone extends CardZone{

    public IntervalZone(Vector3f interval) {
        this.interval = interval;
    }
    private Vector3f interval;

    @Override
    public Vector3f getLocalPosition(Vector3f zonePosition) {
        return zonePosition.mult(interval);
    }
}
