package com.destrostudios.cards.shared.entities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleEntityData implements EntityData {

    public SimpleEntityData(SimpleEntityData data) {
        components = new IntMap[data.components.length];
        for (int i = 0; i < components.length; i++) {
            components[i] = new IntMap(data.components[i]);
        }
        nextEntity = new AtomicInteger(data.getNextEntity());
    }

    public SimpleEntityData(List<ComponentDefinition<?>> componentDefinitions) {
        components = new IntMap[componentDefinitions.size()];
        for (int i = 0; i < components.length; i++) {
            components[i] = new IntMap();
        }
        nextEntity = new AtomicInteger(0);
    }
    private IntMap[] components;
    private AtomicInteger nextEntity;

    @Override
    public int createEntity() {
        return nextEntity.getAndIncrement();
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
    }

    @Override
    public <T> void removeComponent(int entity, ComponentDefinition<T> component) {
        getComponentMap(component).remove(entity);
    }

    @Override
    public Aggregator query(ComponentDefinition<?> component) {
        return new SimpleAggregator(getComponentMap(component));
    }

    private <T> IntMap<T> getComponentMap(ComponentDefinition<T> component) {
        return (IntMap<T>) components[component.getId()];
    }

    public IntMap[] getComponents() {
        return components;
    }

    public void setNextEntity(int value) {
        nextEntity.set(value);
    }

    public int getNextEntity() {
        return nextEntity.get();
    }
}
