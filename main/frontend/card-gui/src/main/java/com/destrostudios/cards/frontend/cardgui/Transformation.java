package com.destrostudios.cards.frontend.cardgui;

public abstract class Transformation<ObjectType, ValueType> implements GameLoopListener {

    protected ObjectType object;

    public void setObject(ObjectType object) {
        this.object = object;
    }

    @Override
    public void update(float lastTimePerFrame) {
        ValueType currentValue = getCurrentValue();
        ValueType newValue = getNewValue(currentValue, lastTimePerFrame);
        applyNewValue(newValue);
    }

    public abstract ValueType getCurrentValue();

    public abstract ValueType getNewValue(ValueType currentValue, float lastTimePerFrame);

    public abstract void applyNewValue(ValueType newValue);

    public abstract boolean hasReachedTarget();
}
