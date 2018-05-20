package com.destrostudios.cards.frontend.cardgui.transformations.handlers;

import com.jme3.math.Quaternion;

public class RotationTransformationHandler extends TargetedTransformationHandler<Quaternion> {

    public RotationTransformationHandler() {
        super(new Quaternion());
    }

    @Override
    public void setValue(Quaternion destinationValue, Quaternion sourceValue) {
        destinationValue.set(sourceValue);
    }

    @Override
    public void compositeValue(Quaternion destinationValue, Quaternion sourceValue) {
        destinationValue.multLocal(sourceValue);
    }
}
