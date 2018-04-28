package com.destrostudios.cards.shared.entities;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.entities.collections.IntToIntMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 *
 * @author Philipp
 */
public class EntityData {

    private final IntToIntMap[] components;

    protected EntityData(int componentCount) {
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

    protected void foreachEntityWithComponent(int component, IntIntConsumer consumer) {
        components[component].foreach(consumer);
    }

    @Deprecated
    @SafeVarargs
    public final List<ComponentValue> entityComponentValues(int component, Predicate<ComponentValue>... predicates) {
        IntToIntMap map = components[component];
        List<ComponentValue> list = new ArrayList<>(map.size());
        map.foreach((entity, value) -> {
            ComponentValue componentValue = new ComponentValue(entity, value);
            for (Predicate<ComponentValue> predicate : predicates) {
                if (!predicate.test(componentValue)) {
                    return;
                }
            }
            list.add(componentValue);
        });
        list.sort(Comparator.comparingInt(x -> x.getEntity()));
        return list;
    }

    public IntArrayList entitiesWithComponent(int component, IntPredicate... predicates) {
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

    public int uniqueEntityWithComponent(int component, IntPredicate... predicates) {
        boolean found = false;
        int result = 0;
        IntToIntMap map = components[component];
        PrimitiveIterator.OfInt iterator = map.iterator();
        while (iterator.hasNext()) {
            int entity = iterator.nextInt();
            for (IntPredicate predicate : predicates) {
                if (!predicate.test(entity)) {
                    if (found) {
                        throw new IllegalStateException("multiple entities found");
                    }
                    found = true;
                    result = entity;
                }
            }
        }
        if (!found) {
            throw new NullPointerException("no entity found");
        }
        return result;
    }
}
