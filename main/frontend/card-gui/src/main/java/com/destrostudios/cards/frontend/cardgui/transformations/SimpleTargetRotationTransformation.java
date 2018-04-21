package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.FloatInterpolate;
import com.jme3.math.Quaternion;

public class SimpleTargetRotationTransformation extends RotationTransformation {

    public SimpleTargetRotationTransformation(Quaternion targetRotation) {
        this.targetRotation = targetRotation;
    }
    protected Quaternion targetRotation;

    @Override
    public Quaternion getNewValue(Quaternion currentValue, float lastTimePerFrame) {
        float speed = 0.25f;
        return FloatInterpolate.get(currentValue, targetRotation, speed, lastTimePerFrame);
    }

    @Override
    public boolean hasReachedTarget() {
        return object.getCurrentRotation().equals(targetRotation);
    }
}
