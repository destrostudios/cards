package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.Transformation;
import com.destrostudios.cards.frontend.cardgui.TransformedBoardObject;
import com.jme3.math.Quaternion;

public abstract class RotationTransformation extends Transformation<TransformedBoardObject, Quaternion> {

    @Override
    public Quaternion getCurrentValue() {
        return object.getCurrentRotation();
    }

    @Override
    public void applyNewValue(Quaternion newValue) {
        object.setCurrentRotation(newValue);
    }
}
