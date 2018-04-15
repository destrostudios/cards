package com.destrostudios.cards.frontend.cardgui.zones;

import com.destrostudios.cards.frontend.cardgui.*;
import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public class CustomZone extends CardZone{

    @Override
    public Vector3f getWorldPosition(Vector3f zonePosition) {
        return zonePosition;
    }
}
