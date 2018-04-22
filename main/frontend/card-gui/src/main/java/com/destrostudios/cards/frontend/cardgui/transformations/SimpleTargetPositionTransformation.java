package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.FloatInterpolate;
import com.destrostudios.cards.frontend.cardgui.transformations.speeds.TimeBasedPositionTransformationSpeed;
import com.jme3.math.Vector3f;

public class SimpleTargetPositionTransformation extends PositionTransformation {

    public SimpleTargetPositionTransformation(Vector3f targetPosition) {
        this(targetPosition, new TimeBasedPositionTransformationSpeed(1.5f));
    }

    public SimpleTargetPositionTransformation(Vector3f targetPosition, TransformationSpeed<Vector3f> transformationSpeed) {
        this.transformationSpeed = transformationSpeed;
        setTargetPosition(targetPosition, true);
    }
    private Vector3f targetPosition = new Vector3f();
    private TransformationSpeed<Vector3f> transformationSpeed;

    public void setTargetPosition(Vector3f targetPosition, boolean resetSpeed) {
        if (!targetPosition.equals(this.targetPosition)) {
            this.targetPosition.set(targetPosition);
            if (resetSpeed) {
                transformationSpeed.reset();
            }
        }
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        transformationSpeed.update(lastTimePerFrame);
    }

    @Override
    public Vector3f getNewValue(Vector3f currentValue, float lastTimePerFrame) {
        float speed = transformationSpeed.getSpeed(currentValue, targetPosition);
        return FloatInterpolate.get(currentValue, targetPosition, speed, lastTimePerFrame);
    }

    @Override
    public boolean hasReachedTarget() {
        return object.getCurrentPosition().equals(targetPosition);
    }

    @Override
    public SimpleTargetPositionTransformation clone() {
        return new SimpleTargetPositionTransformation(targetPosition, transformationSpeed.clone());
    }
}
