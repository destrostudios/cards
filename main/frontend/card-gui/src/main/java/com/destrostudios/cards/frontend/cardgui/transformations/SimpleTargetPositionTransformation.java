package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.FloatInterpolate;
import com.jme3.math.Vector3f;

public class SimpleTargetPositionTransformation extends PositionTransformation {

    public SimpleTargetPositionTransformation(Vector3f targetPosition) {
        this.targetPosition = targetPosition;
    }
    protected Vector3f targetPosition;

    @Override
    public Vector3f getNewValue(Vector3f currentValue, float lastTimePerFrame) {
        float distance = currentValue.distance(targetPosition);
        float speed = Math.max(2, distance);
        return FloatInterpolate.get(currentValue, targetPosition, speed, lastTimePerFrame);
    }

    @Override
    public boolean hasReachedTarget() {
        return object.getCurrentPosition().equals(targetPosition);
    }
}
