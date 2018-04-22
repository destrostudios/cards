package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.Transformation;
import com.destrostudios.cards.frontend.cardgui.TransformedBoardObject;
import com.jme3.math.Vector3f;

public abstract class PositionTransformation extends Transformation<TransformedBoardObject, Vector3f> {

    @Override
    public Vector3f getCurrentValue() {
        return object.getCurrentPosition();
    }

    @Override
    public void applyNewValue(Vector3f newValue) {
        object.setCurrentPosition(newValue);
    }

    @Override
    public abstract PositionTransformation clone();
}
