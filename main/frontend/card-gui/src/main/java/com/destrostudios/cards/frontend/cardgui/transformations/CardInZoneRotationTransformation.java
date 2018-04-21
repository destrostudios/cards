package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.ZonePosition;
import com.jme3.math.Quaternion;

public class CardInZoneRotationTransformation extends SimpleTargetRotationTransformation {

    public CardInZoneRotationTransformation(ZonePosition zonePosition) {
        super(new Quaternion());
        this.zonePosition = zonePosition;
    }
    private ZonePosition zonePosition;

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        targetRotation.set(zonePosition.getDefaultTargetRotation());
    }
}
