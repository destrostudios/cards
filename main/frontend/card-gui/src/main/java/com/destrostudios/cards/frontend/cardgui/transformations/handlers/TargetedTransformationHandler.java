package com.destrostudios.cards.frontend.cardgui.transformations.handlers;

import com.destrostudios.cards.frontend.cardgui.transformations.TargetedTransformation;

public abstract class TargetedTransformationHandler<ValueType> extends StatePreservingTransformationHandler<ValueType, TargetedTransformation<ValueType>> {

    public TargetedTransformationHandler(ValueType value) {
        super(value);
    }

    @Override
    public void setCurrentValue(ValueType currentValue) {
        super.setCurrentValue(currentValue);
    }

    public boolean hasReachedTarget() {
        return ((transformation != null) ? transformation.hasReachedTarget() : true);
    }
}
