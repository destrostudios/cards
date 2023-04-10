package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

    public static List<int[]> getAllSubsets(List<Integer> list) {
        List<int[]> subsets = new LinkedList<>();
        addAllSubsets(list, -1, null, subsets);
        return subsets;
    }

    private static void addAllSubsets(List<Integer> list, int offset, int[] offsetSubset, List<int[]> subsets) {
        int[] subset;
        if (offset == -1) {
            subset = new int[0];
        } else {
            subset = new int[offsetSubset.length + 1];
            System.arraycopy(offsetSubset, 0, subset, 0, offsetSubset.length);
            subset[subset.length - 1] = list.get(offset);
        }
        subsets.add(subset);
        for (int i = offset + 1; i < list.size(); i++) {
            addAllSubsets(list, i, subset, subsets);
        }
    }
}
