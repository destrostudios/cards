package com.destrostudios.cards.shared.entities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

public class SimpleEntityData implements EntityData {

    public SimpleEntityData(List<ComponentDefinition<?>> componentDefinitions) {
        components = new IntMap[componentDefinitions.size()];
        mutable = new boolean[components.length];
        cacheList = new IntList[components.length];
        for (int i = 0; i < components.length; i++) {
            components[i] = new IntMap(16); // Picking this optimally greatly improves performance by doing less resizes (important for bot!)
        }
        nextEntity = 0;
    }
    private IntMap[] components;
    private boolean[] mutable;
    private IntList[] cacheList;
    private int nextEntity;

    @Override
    public int createEntity() {
        return nextEntity++;
    }

    @Override
    public boolean hasComponent(int entity, ComponentDefinition<?> component) {
        return getComponentMapForRead(component).hasKey(entity);
    }

    @Override
    public <T> T getComponent(int entity, ComponentDefinition<T> component) {
        return ((IntMap<T>) getComponentMapForRead(component)).get(entity);
    }

    @Override
    public <T> void setComponent(int entity, ComponentDefinition<T> component, T value) {
        ((IntMap<T>) getComponentMapForWrite(component)).set(entity, value);
        cacheList[component.getId()] = null;
    }

    @Override
    public <T> void removeComponent(int entity, ComponentDefinition<T> component) {
        getComponentMapForWrite(component).remove(entity);
        cacheList[component.getId()] = null;
    }

    @Override
    public IntList list(ComponentDefinition<?> component) {
        IntList result = cacheList[component.getId()];
        if (result == null) {
            IntMap<?> componentMap = getComponentMapForRead(component);
            result = new IntList(componentMap.size());
            componentMap.foreachKey(result::add);
            result.sort();
            cacheList[component.getId()] = result;
        }
        return result;
    }

    @Override
    public IntList list(ComponentDefinition<?> component, IntPredicate predicate) {
        IntMap<?> componentMap = getComponentMapForRead(component);
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
            componentMaps[i] = getComponentMapForRead(components[i]);
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
        return getComponentMapForRead(component).iterator().next();
    }

    @Override
    public int count(ComponentDefinition<?> component) {
        return getComponentMapForRead(component).size();
    }

    @Override
    public int count(ComponentDefinition<?> component, IntPredicate predicate) {
        AtomicInteger count = new AtomicInteger();
        getComponentMapForRead(component).foreachKey(value -> {
            if (predicate.test(value)) {
                count.getAndIncrement();
            }
        });
        return count.get();
    }

    private IntMap<?> getComponentMapForWrite(ComponentDefinition<?> component) {
        if (!mutable[component.getId()]) {
            IntMap<?> componentMap = new IntMap(components[component.getId()]);
            components[component.getId()] = componentMap;
            mutable[component.getId()] = true;
            return componentMap;
        }
        return components[component.getId()];
    }

    private IntMap<?> getComponentMapForRead(ComponentDefinition<?> component) {
        return components[component.getId()];
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
        System.arraycopy(source.components, 0, components, 0, components.length);
        Arrays.fill(mutable, false);
        System.arraycopy(source.cacheList, 0, cacheList, 0, components.length);
        nextEntity = source.nextEntity;
    }
}
