package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;

import java.util.ArrayList;
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

    public static boolean equalsUnsortedAndUnique(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int value1 : array1) {
            boolean contained = false;
            for (int value2 : array2) {
                if (value1 == value2) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                return false;
            }
        }
        return true;
    }

    public static List<int[]> getAllSubsets(IntList list) {
        List<int[]> subsets = new ArrayList<>(1 << list.size());
        addAllSubsets(list, -1, null, subsets);
        return subsets;
    }

    private static void addAllSubsets(IntList list, int offset, int[] offsetSubset, List<int[]> subsets) {
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

    // https://stackoverflow.com/questions/54242442/is-there-a-method-returns-all-possible-combinations-for-n-choose-k
    public static <T> List<IntList> getSubsets(IntList list, int amount) {
        ArrayList<IntList> subsets = new ArrayList<>();
        addSubsets(list, amount, 0, new IntList(amount), subsets);
        return subsets;
    }

    private static void addSubsets(IntList superSet, int amount, int index, IntList current, ArrayList<IntList> subsets) {
        if (current.size() == amount) {
            subsets.add(new IntList(current));
            return;
        }
        if (index == superSet.size()) {
            return;
        }
        int value = superSet.get(index);
        current.add(value);
        addSubsets(superSet, amount, index + 1, current, subsets);
        current.removeFirst(value);
        addSubsets(superSet, amount, index + 1, current, subsets);
    }
}
