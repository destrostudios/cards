package com.destrostudios.cards.shared.entities;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;

/**
 *
 * @author Philipp
 */
public class SimpleEntityData implements EntityData {

    private final Map<ComponentDefinition<?>, Map<Integer, Object>> components = new HashMap<>();
    private final IntSupplier entitySequence;

    public SimpleEntityData() {
        this(new AtomicInteger(0)::incrementAndGet);
    }

    public SimpleEntityData(IntSupplier entitySequence) {
        this.entitySequence = entitySequence;
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
    public IntArrayList entities(ComponentDefinition<?> component, IntPredicate... predicates) {
        Map<Integer, ?> map = getComponentMap(component);
        IntArrayList list = new IntArrayList(map.size());
        map.keySet().forEach(entity -> {
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

    @Override
    public int createEntity() {
        return entitySequence.getAsInt();
    }

    Set<ComponentDefinition<?>> knownComponents() {
        return components.keySet();
    }

    Set<Integer> getEntities() {
        Set<Integer> result = new HashSet<>();
        for (Map<Integer, Object> componentMap : components.values()) {
            result.addAll(componentMap.keySet());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> Map<Integer, T> getComponentMap(ComponentDefinition<T> component) {
        return (Map<Integer, T>) components.computeIfAbsent(component, x -> new HashMap<>());
    }
}
