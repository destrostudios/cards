package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.GameLoopListener;

public abstract class TransformationSpeed<T> implements Cloneable, GameLoopListener {

    public void reset() {

    }

    @Override
    public void update(float lastTimePerFrame) {

    }

    public abstract float getSpeed(T currentValue, T targetValue);

    @Override
    public abstract TransformationSpeed<T> clone();
}
