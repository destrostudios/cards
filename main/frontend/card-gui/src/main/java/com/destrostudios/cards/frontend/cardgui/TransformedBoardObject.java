package com.destrostudios.cards.frontend.cardgui;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public abstract class TransformedBoardObject extends BoardObject {

    private Vector3f currentPosition;
    private Quaternion currentRotation;
    private Vector3f fixedTargetWorldPosition;
    private Quaternion fixedTargetRotation;

    public boolean hasReachedTargetTransform() {
        return (hasReachedTargetPosition() && hasReachedTargetRotation());
    }

    public boolean hasReachedTargetPosition() {
        return currentPosition.equals(getTargetPosition());
    }

    public boolean hasReachedTargetRotation() {
        return currentRotation.equals(getTargetRotation());
    }

    public void setCurrentPosition(Vector3f currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Vector3f getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentRotation(Quaternion currentRotation) {
        this.currentRotation = currentRotation;
    }

    public Quaternion getCurrentRotation() {
        return currentRotation;
    }

    public void setFixedTargetWorldPosition(Vector3f fixedTargetWorldPosition) {
        this.fixedTargetWorldPosition = fixedTargetWorldPosition;
    }

    public void setFixedTargetRotation(Quaternion fixedTargetRotation) {
        this.fixedTargetRotation = fixedTargetRotation;
    }

    public void resetFixedTargetTransform() {
        fixedTargetWorldPosition = null;
        fixedTargetRotation = null;
    }

    public Vector3f getTargetPosition() {
        return ((fixedTargetWorldPosition != null) ? fixedTargetWorldPosition : getDefaultTargetPosition());
    }

    public Quaternion getTargetRotation() {
        return ((fixedTargetRotation != null) ? fixedTargetRotation : getDefaultTargetRotation());
    }

    protected abstract Vector3f getDefaultTargetPosition();

    protected abstract Quaternion getDefaultTargetRotation();
}
