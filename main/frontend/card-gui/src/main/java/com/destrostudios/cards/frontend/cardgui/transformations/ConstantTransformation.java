package com.destrostudios.cards.frontend.cardgui.transformations;

public abstract class ConstantTransformation<ValueType> extends StatefulTransformation<ValueType> {

    public ConstantTransformation(ValueType value) {
        super(value);
    }

    @Override
    public void update(float lastTimePerFrame) {

    }
}
