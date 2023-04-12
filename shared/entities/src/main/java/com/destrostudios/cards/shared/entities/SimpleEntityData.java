package com.destrostudios.cards.shared.entities;

import java.util.*;

public class SimpleEntityData implements EntityData {

    public SimpleEntityData(SimpleEntityData data) {
        components = new IntMap[data.components.length];
        for (int i = 0; i < components.length; i++) {
            components[i] = new IntMap(data.components[i]);
        }
        nextEntity = data.getNextEntity();
    }

    public SimpleEntityData(List<ComponentDefinition<?>> componentDefinitions) {
        components = new IntMap[componentDefinitions.size()];
        for (int i = 0; i < components.length; i++) {
            components[i] = new IntMap();
        }
        nextEntity = 0;
    }
    private IntMap[] components;
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
    }

    @Override
    public <T> void removeComponent(int entity, ComponentDefinition<T> component) {
        getComponentMap(component).remove(entity);
    }

    @Override
    public Aggregator query(ComponentDefinition<?> component) {
        return new MapAggregator(getComponentMap(component));
    }

    @Override
    public Aggregator queryAll(ComponentDefinition<?>... components) {
        IntMap<?>[] componentMaps = new IntMap[components.length];
        for (int i = 0; i < components.length; i++) {
            componentMaps[i] = getComponentMap(components[i]);
        }
        return new MultiMapAggregator(componentMaps);
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
}
