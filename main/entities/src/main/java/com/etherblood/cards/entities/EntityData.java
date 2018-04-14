package com.etherblood.cards.entities;

import com.etherblood.cards.entities.collections.IntToIntMap;

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

    public int getOrElse(int entity, int component, int defaultValue) {
        return components[component].getOrElse(entity, defaultValue);
    }

    public void set(int entity, int component, int value) {
        components[component].set(entity, value);
    }

    public void remove(int entity, int component) {
        components[component].remove(entity);
    }

    public void foreachEntityWithComponent(int component, IntIntConsumer consumer) {
        components[component].foreach(consumer);
    }

//    public IntStream streamEntitiesWithComponent(int component, IntIntConsumer consumer) {
//        return components[component].stream();
//    }
    
}
