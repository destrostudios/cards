package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;

public class ArrayUtil {

    public static void add(EntityData data, int entity, ComponentDefinition<int[]> component, int value) {
        int[] oldArray = data.getOptionalComponent(entity, component).orElse(new int[0]);
        int[] newArray = new int[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = value;
        data.setComponent(entity, component, newArray);
    }

    public static void remove(EntityData data, int entity, ComponentDefinition<int[]> component, int value) {
        int[] oldArray = data.getComponent(entity, component);
        if (oldArray != null) {
            Integer valueIndex = null;
            for (int i = 0; i < oldArray.length; i++) {
                if (oldArray[i] == value) {
                    valueIndex = i;
                    break;
                }
            }
            if (valueIndex != null) {
                if (oldArray.length > 1) {
                    int[] newArray = new int[oldArray.length - 1];
                    System.arraycopy(oldArray, 0, newArray, 0, valueIndex);
                    System.arraycopy(oldArray, valueIndex + 1, newArray, 0, oldArray.length - valueIndex - 1);
                    data.setComponent(entity, component, newArray);
                } else {
                    data.removeComponent(entity, component);
                }
            }
        }
    }

    public static boolean contains(EntityData data, int entity, ComponentDefinition<int[]> component, int value) {
        int[] array = data.getComponent(entity, component);
        return ((array != null) && contains(array, value));
    }

    public static boolean contains(int[] array, int value) {
        for (int arrayValue : array) {
            if (arrayValue == value) {
                return true;
            }
        }
        return false;
    }
}
