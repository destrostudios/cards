package com.destrostudios.cards.shared.rules.expressions;

import java.util.Arrays;

public class ExpressionGlobals {

    public static <T> T[] join(T[] array1, T[] array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
}
