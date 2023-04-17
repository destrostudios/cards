package com.destrostudios.cards.shared.entities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

public class SimpleEntityData implements EntityData {

    public SimpleEntityData(List<ComponentDefinition<?>> componentDefinitions) {
        components = new IntMap[componentDefinitions.size()];
        cacheList = new IntList[components.length];
        for (int i = 0; i < components.length; i++) {
            components[i] = new IntMap(16); // Picking this optimally greatly improves performance by doing less resizes (important for bot!)
        }
        nextEntity = 0;
    }
    private IntMap[] components;
    private IntList[] cacheList;
    private int nextEntity;

    @Override
    public int createEntity() {
        return nextEntity++;
    }

    @Override
    public boolean hasComponent(int entity, ComponentDefinition<?> component) {
        return getComponentMap(component).hasKey(entity);
    }

    @Override
    public <T> T getComponent(int entity, ComponentDefinition<T> component) {
        return getComponentMap(component).get(entity);
    }

    @Override
    public <T> void setComponent(int entity, ComponentDefinition<T> component, T value) {
        getComponentMap(component).set(entity, value);
        cacheList[component.getId()] = null;
    }

    @Override
    public <T> void removeComponent(int entity, ComponentDefinition<T> component) {
        getComponentMap(component).remove(entity);
        cacheList[component.getId()] = null;
    }

    @Override
    public IntList list(ComponentDefinition<?> component) {
        IntList result = cacheList[component.getId()];
        if (result == null) {
            IntMap<?> componentMap = components[component.getId()];
            result = new IntList(componentMap.size());
            componentMap.foreachKey(result::add);
            result.sort();
            cacheList[component.getId()] = result;
        }
        return result;
    }

    @Override
    public IntList list(ComponentDefinition<?> component, IntPredicate predicate) {
        IntMap<?> componentMap = components[component.getId()];
        IntList result = new IntList(componentMap.size());
        componentMap.foreachKey(value -> {
            if (predicate.test(value)) {
                result.add(value);
            }
        });
        result.sort();
        return result;
    }

    @Override
    public IntList listAll(ComponentDefinition<?>... components) {
        IntList result = listAllUnsorted(components);
        result.sort();
        return result;
    }

    @Override
    public IntList listAll(ComponentDefinition<?>[] components, IntPredicate predicate) {
        IntList result = listAllUnsorted(components);
        result.retain(predicate);
        result.sort();
        return result;
    }

    private IntList listAllUnsorted(ComponentDefinition<?>... components) {
        IntMap<?>[] componentMaps = new IntMap[components.length];
        for (int i = 0; i < components.length; i++) {
            componentMaps[i] = getComponentMap(components[i]);
        }
        IntList result = new IntList(componentMaps[0].size());
        componentMaps[0].foreachKey(key -> {
            for (int i = 1; i < componentMaps.length; i++) {
                if (!componentMaps[i].hasKey(key)) {
                    return;
                }
            }
            result.add(key);
        });
        return result;
    }

    @Override
    public int unique(ComponentDefinition<?> component) {
        return getComponentMap(component).iterator().next();
    }

    @Override
    public int count(ComponentDefinition<?> component) {
        return getComponentMap(component).size();
    }

    @Override
    public int count(ComponentDefinition<?> component, IntPredicate predicate) {
        AtomicInteger count = new AtomicInteger();
        getComponentMap(component).foreachKey(value -> {
            if (predicate.test(value)) {
                count.getAndIncrement();
            }
        });
        return count.get();
    }

    private <T> IntMap<T> getComponentMap(ComponentDefinition<T> component) {
        return (IntMap<T>) components[component.getId()];
    }

    public IntMap[] getComponents() {
        return components;
    }

    public void setNextEntity(int nextEntity) {
        this.nextEntity = nextEntity;
    }

    public int getNextEntity() {
        return nextEntity;
    }

    public void copyFrom(SimpleEntityData source) {
        for (int i = 0; i < components.length; i++) {
            components[i].copyFrom(source.components[i]);
        }
        System.arraycopy(source.cacheList, 0, cacheList, 0, components.length);
        nextEntity = source.nextEntity;
    }
}
