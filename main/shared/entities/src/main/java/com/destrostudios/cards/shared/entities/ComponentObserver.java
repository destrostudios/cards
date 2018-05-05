package com.destrostudios.cards.shared.entities;

/**
 *
 * @author Philipp
 */
public interface ComponentObserver<T> {
    void onValueAdded(int entity, T value);
    void onValueChanged(int entity, T newValue, T oldValue);
    void onValueRemoved(int entity, T value);
}
