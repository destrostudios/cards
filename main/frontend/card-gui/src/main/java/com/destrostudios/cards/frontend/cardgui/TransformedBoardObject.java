package com.destrostudios.cards.frontend.cardgui;

import com.destrostudios.cards.frontend.cardgui.transformations.*;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public abstract class TransformedBoardObject extends BoardObject {

    private Vector3f currentPosition = new Vector3f();
    private Quaternion currentRotation = new Quaternion();
    private PositionTransformation positionTransformation;
    private RotationTransformation rotationTransformation;
    private boolean isTransformationEnabled = true;

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        if (isTransformationEnabled) {
            // Position
            if (positionTransformation == null) {
                setPositionTransformation(getDefaultPositionTransformation());
            }
            if (positionTransformation != null) {
                positionTransformation.update(lastTimePerFrame);
            }
            // Rotation
            if (rotationTransformation == null) {
                setRotationTransformation(getDefaultRotationTransformation());
            }
            if (rotationTransformation != null) {
                rotationTransformation.update(lastTimePerFrame);
            }
        }
    }

    public void resetTransformations() {
        setPositionTransformation(null);
        setRotationTransformation(null);
    }

    public void setPositionTransformation(PositionTransformation positionTransformation) {
        if (positionTransformation != null) {
            positionTransformation.setObject(this);
        }
        this.positionTransformation = positionTransformation;
    }

    public void setRotationTransformation(RotationTransformation rotationTransformation) {
        if (rotationTransformation != null) {
            rotationTransformation.setObject(this);
        }
        this.rotationTransformation = rotationTransformation;
    }

    public boolean hasReachedTargetTransform() {
        return (hasReachedTargetPosition() && hasReachedTargetRotation());
    }

    public boolean hasReachedTargetPosition() {
        return ((positionTransformation != null) ? positionTransformation.hasReachedTarget() : true);
    }

    public boolean hasReachedTargetRotation() {
        return ((rotationTransformation != null) ? rotationTransformation.hasReachedTarget() : true);
    }

    protected PositionTransformation getDefaultPositionTransformation() {
        return null;
    }

    protected RotationTransformation getDefaultRotationTransformation() {
        return null;
    }

    public void setCurrentPosition(Vector3f currentPosition) {
        this.currentPosition.set(currentPosition);
    }

    public Vector3f getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentRotation(Quaternion currentRotation) {
        this.currentRotation.set(currentRotation);
    }

    public Quaternion getCurrentRotation() {
        return currentRotation;
    }

    public void setTransformationEnabled(boolean transformationEnabled) {
        this.isTransformationEnabled = transformationEnabled;
    }
}
