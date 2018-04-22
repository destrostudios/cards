package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.FloatInterpolate;
import com.destrostudios.cards.frontend.cardgui.transformations.speeds.TimeBasedRotationTransformationSpeed;
import com.jme3.math.Quaternion;

public class SimpleTargetRotationTransformation extends RotationTransformation {

    public SimpleTargetRotationTransformation(Quaternion targetRotation) {
        this(targetRotation, new TimeBasedRotationTransformationSpeed(1));
    }

    public SimpleTargetRotationTransformation(Quaternion targetRotation, TransformationSpeed<Quaternion> transformationSpeed) {
        this.transformationSpeed = transformationSpeed;
        setTargetRotation(targetRotation, true);
    }
    private Quaternion targetRotation = new Quaternion();
    private TransformationSpeed<Quaternion> transformationSpeed;

    public void setTargetRotation(Quaternion targetRotation, boolean resetSpeed) {
        if (!targetRotation.equals(this.targetRotation)) {
            this.targetRotation.set(targetRotation);
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
    public Quaternion getNewValue(Quaternion currentValue, float lastTimePerFrame) {
        float speed = transformationSpeed.getSpeed(currentValue, targetRotation);
        return FloatInterpolate.get(currentValue, targetRotation, speed, lastTimePerFrame);
    }

    @Override
    public boolean hasReachedTarget() {
        return object.getCurrentRotation().equals(targetRotation);
    }

    @Override
    public SimpleTargetRotationTransformation clone() {
        return new SimpleTargetRotationTransformation(targetRotation, transformationSpeed.clone());
    }
}
