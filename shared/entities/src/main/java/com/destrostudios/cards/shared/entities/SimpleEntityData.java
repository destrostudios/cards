package com.destrostudios.cards.shared.entities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleEntityData implements EntityData {

    public SimpleEntityData(SimpleEntityData data) {
        components = new HashMap[data.components.length];
        for (int i = 0; i < components.length; i++) {
            components[i] = new HashMap<>(data.components[i]);
        }
        nextEntity = new AtomicInteger(data.getNextEntity());
    }

    public SimpleEntityData(List<ComponentDefinition<?>> componentDefinitions) {
        components = new HashMap[componentDefinitions.size()];
        for (int i = 0; i < components.length; i++) {
            components[i] = new HashMap<>();
        }
        nextEntity = new AtomicInteger(0);
    }
    private HashMap<Integer, Object>[] components;
    private AtomicInteger nextEntity;

    @Override
    public int createEntity() {
        return nextEntity.getAndIncrement();
    }

    @Override
    public boolean hasComponent(int entity, ComponentDefinition<?> component) {
        return getComponentMap(component).containsKey(entity);
    }

    @Override
    public <T> T getComponent(int entity, ComponentDefinition<T> component) {
        return getComponentMap(component).get(entity);
    }

    @Override
    public <T> void setComponent(int entity, ComponentDefinition<T> component, T value) {
        getComponentMap(component).put(entity, value);
    }

    @Override
    public <T> void removeComponent(int entity, ComponentDefinition<T> component) {
        getComponentMap(component).remove(entity);
    }

    @Override
    public <T> Aggregator<T> query(ComponentDefinition<T> component) {
        return new SimpleAggregator<>(getComponentMap(component));
    }

    @SuppressWarnings("unchecked")
    private <T> Map<Integer, T> getComponentMap(ComponentDefinition<T> component) {
        return (Map<Integer, T>) components[component.getId()];
    }

    public Map<Integer, Object>[] getComponents() {
        return components;
    }

    public void setNextEntity(int value) {
        nextEntity.set(value);
    }

    public int getNextEntity() {
        return nextEntity.get();
    }
}
