package com.destrostudios.cards.frontend.cardgui.transformations.relative;

import com.destrostudios.cards.frontend.cardgui.GameLoopListener;
import com.destrostudios.cards.frontend.cardgui.transformations.Transformation;

public class RelativeTransformation<ValueType> extends Transformation<ValueType> implements GameLoopListener {

    public RelativeTransformation(Transformation<ValueType> transformation) {
        this.transformation = transformation;
    }
    protected Transformation<ValueType> transformation;
    private boolean isEnabled = true;

    @Override
    public void update(float lastTimePerFrame) {
        if (isEnabled) {
            transformation.update(lastTimePerFrame);
        }
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public ValueType getCurrentValue() {
        return transformation.getCurrentValue();
    }
}
