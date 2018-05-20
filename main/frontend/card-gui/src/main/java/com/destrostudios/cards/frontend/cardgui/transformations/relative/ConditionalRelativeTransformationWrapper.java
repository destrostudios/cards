package com.destrostudios.cards.frontend.cardgui.transformations.relative;

import com.destrostudios.cards.frontend.cardgui.transformations.Transformation;

import java.util.function.BooleanSupplier;

public class ConditionalRelativeTransformationWrapper<ValueType> extends RelativeTransformation<ValueType> {

    public ConditionalRelativeTransformationWrapper(Transformation<ValueType> targetedTransformation, BooleanSupplier condition) {
        super(targetedTransformation);
        this.condition = condition;
    }
    private BooleanSupplier condition;

    @Override
    public void update(float lastTimePerFrame) {
        setEnabled(condition.getAsBoolean());
        super.update(lastTimePerFrame);
    }
}
