package com.destrostudios.cards.frontend.cardgui.transformations.relative;

import com.destrostudios.cards.frontend.cardgui.GameLoopListener;
import com.destrostudios.cards.frontend.cardgui.transformations.SimpleTargetedTransformation;
import com.destrostudios.cards.frontend.cardgui.transformations.StatefulTransformation;
import com.destrostudios.cards.frontend.cardgui.transformations.Transformation;

public class RelativeTransformation<ValueType> extends Transformation<ValueType> implements GameLoopListener {

    public RelativeTransformation(StatefulTransformation<ValueType> transformation, SimpleTargetedTransformation<ValueType> resetTransformation) {
        this.transformation = transformation;
        this.resetTransformation = resetTransformation;
    }
    private StatefulTransformation<ValueType> transformation;
    private SimpleTargetedTransformation<ValueType> resetTransformation;
    private boolean isEnabled = true;

    @Override
    public void update(float lastTimePerFrame) {
        if (isEnabled) {
            transformation.update(lastTimePerFrame);
        }
        else {
            resetTransformation.update(lastTimePerFrame);
        }
    }

    public void setEnabled(boolean isEnabled) {
        if (isEnabled != this.isEnabled) {
            this.isEnabled = isEnabled;
            if (isEnabled) {
                transformation.setCurrentValue(resetTransformation.getCurrentValue());
                if (transformation instanceof SimpleTargetedTransformation) {
                    SimpleTargetedTransformation simpleTargetedTransformation = (SimpleTargetedTransformation) transformation;
                    simpleTargetedTransformation.resetSpeed();
                }
            }
            else {
                resetTransformation.setCurrentValue(transformation.getCurrentValue());
                resetTransformation.resetSpeed();
            }
        }
    }

    @Override
    public ValueType getCurrentValue() {
        return (isEnabled ? transformation.getCurrentValue() : resetTransformation.getCurrentValue());
    }
}
