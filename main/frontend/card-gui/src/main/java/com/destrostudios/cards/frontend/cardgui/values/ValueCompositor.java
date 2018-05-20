package com.destrostudios.cards.frontend.cardgui.values;

public interface ValueCompositor<ValueType> {
    void compositeValue(ValueType destinationValue, ValueType sourceValue);
}
