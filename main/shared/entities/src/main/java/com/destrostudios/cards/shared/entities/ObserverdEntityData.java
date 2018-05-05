package com.destrostudios.cards.shared.entities;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;

public class ObserverdEntityData implements EntityData, HasEntityData {

    private final EntityData data;
    private final Map<ComponentDefinition<?>, Object> observers = new HashMap<>();

    public ObserverdEntityData(EntityData data) {
        this.data = data;
    }

    @Override
    public int createEntity() {
        return data.createEntity();
    }

    @Override
    public boolean hasComponent(int entity, ComponentDefinition<?> component) {
        return data.hasComponent(entity, component);
    }

    @Override
    public <T> T getComponent(int entity, ComponentDefinition<T> component) {
        return data.getComponent(entity, component);
    }

    @Override
    public <T> void setComponent(int entity, ComponentDefinition<T> component, T value) {
        if (data.hasComponent(entity, component)) {
            T old = data.getComponent(entity, component);
            data.setComponent(entity, component, value);
            for (ComponentObserver<T> observer : getObservers(component)) {
                observer.onValueChanged(entity, value, old);
            }
        } else {
            data.setComponent(entity, component, value);
            for (ComponentObserver<T> observer : getObservers(component)) {
                observer.onValueAdded(entity, value);
            }
        }
    }

    @Override
    public <T> void removeComponent(int entity, ComponentDefinition<T> component) {
        if (data.hasComponent(entity, component)) {
            T old = data.getComponent(entity, component);
            data.removeComponent(entity, component);
            for (ComponentObserver<T> observer : getObservers(component)) {
                observer.onValueRemoved(entity, old);
            }
        }
    }

    @Override
    public IntArrayList entities(ComponentDefinition<?> component, IntPredicate... predicates) {
        return data.entities(component, predicates);
    }

    public <T> void registerObserver(ComponentDefinition<T> component, ComponentObserver<T> observer) {
        getObservers(component).add(observer);
    }

    @Override
    public EntityData getEntityData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    private <T> List<ComponentObserver<T>> getObservers(ComponentDefinition<T> component) {
        return (List<ComponentObserver<T>>) observers.computeIfAbsent(component, c -> new ArrayList<>());
    }

}
