package com.destrostudios.cards.frontend.cardgui.values;

public interface ValueSetter<ValueType> {
    void setValue(ValueType destinationValue, ValueType sourceValue);
}
