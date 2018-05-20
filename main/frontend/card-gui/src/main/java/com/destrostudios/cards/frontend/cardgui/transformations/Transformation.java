package com.destrostudios.cards.frontend.cardgui.transformations;

import com.destrostudios.cards.frontend.cardgui.GameLoopListener;

public abstract class Transformation<ValueType> implements GameLoopListener {
    public abstract ValueType getCurrentValue();
}
