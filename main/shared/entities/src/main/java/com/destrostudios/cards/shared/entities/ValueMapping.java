package com.destrostudios.cards.shared.entities;

/**
 *
 * @author Philipp
 */
public interface ValueMapping<T> {
//    int toInt(T value);

    T fromInt(int value);
}
