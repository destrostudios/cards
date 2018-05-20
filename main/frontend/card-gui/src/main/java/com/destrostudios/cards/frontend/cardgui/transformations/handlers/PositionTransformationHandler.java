package com.destrostudios.cards.frontend.cardgui.transformations.handlers;

import com.jme3.math.Vector3f;

public class PositionTransformationHandler extends TargetedTransformationHandler<Vector3f> {

    public PositionTransformationHandler() {
        super(new Vector3f());
    }

    @Override
    public void setValue(Vector3f destinationValue, Vector3f sourceValue) {
        destinationValue.set(sourceValue);
    }

    @Override
    public void compositeValue(Vector3f destinationValue, Vector3f sourceValue) {
        destinationValue.addLocal(sourceValue);
    }
}
