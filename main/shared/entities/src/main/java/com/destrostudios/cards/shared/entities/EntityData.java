package com.destrostudios.cards.shared.entities;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.entities.collections.IntToIntMap;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class EntityData {

    private final IntToIntMap[] components;

    public EntityData(int componentCount) {
        this.components = new IntToIntMap[componentCount];
        for (int i = 0; i < components.length; i++) {
            components[i] = new IntToIntMap();
        }
    }

    public boolean has(int entity, int component) {
        return components[component].hasKey(entity);
    }

    public boolean hasValue(int entity, int component, int value) {
        return components[component].getOrElse(entity, ~value) == value;
    }

    public int getOrElse(int entity, int component, int defaultValue) {
        return components[component].getOrElse(entity, defaultValue);
    }

    public int get(int entity, int component) {
        return components[component].get(entity);
    }

    public void set(int entity, int component, int value) {
        components[component].set(entity, value);
    }

    public void remove(int entity, int component) {
        components[component].remove(entity);
    }

    public IntArrayList entities(int component, IntPredicate... predicates) {
        IntToIntMap map = components[component];
        IntArrayList list = new IntArrayList(map.size());
        map.foreachKey(entity -> {
            for (IntPredicate predicate : predicates) {
                if (!predicate.test(entity)) {
                    return;
                }
            }
            list.add(entity);
        });
        list.sort();
        return list;
    }
}
