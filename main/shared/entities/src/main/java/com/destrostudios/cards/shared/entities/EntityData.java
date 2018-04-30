package com.destrostudios.cards.shared.entities;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.entities.collections.IntSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;

/**
 *
 * @author Philipp
 */
public class EntityData {

    private final Map<ComponentDefinition<?>, Map<Integer, Object>> components = new HashMap<>();
    private final IntSet entities = new IntSet();
    private final IntSupplier entitySequence;

    public EntityData(IntSupplier entitySequence) {
        this.entitySequence = entitySequence;
    }

    public boolean has(int entity, ComponentDefinition<?> component) {
        return getComponentMap(component).containsKey(entity);
    }

    public <T> boolean hasValue(int entity, ComponentDefinition<T> component, T value) {
        return has(entity, component) && Objects.equals(get(entity, component), value);
    }

    public <T> T getOrElse(int entity, ComponentDefinition<T> component, T defaultValue) {
        return getComponentMap(component).getOrDefault(entity, defaultValue);
    }

    public <T> T get(int entity, ComponentDefinition<T> component) {
        return getComponentMap(component).get(entity);
    }

    public <T> void set(int entity, ComponentDefinition<T> component, T value) {
        getComponentMap(component).put(entity, value);
    }

    public void remove(int entity, ComponentDefinition<?> component) {
        getComponentMap(component).remove(entity);
    }

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

    public int createEntity() {
        int entityKey;
        do {
            entityKey = entitySequence.getAsInt();
        } while (entities.hasKey(entityKey));
        entities.set(entityKey);
        return entityKey;
    }

    public int entity(ComponentDefinition<?> component, IntPredicate... predicates) {
        IntArrayList result = entities(component, predicates);
        if (result.size() != 1) {
            throw new IllegalStateException(Integer.toString(result.size()));
        }
        return result.get(0);
    }

    public Set<ComponentDefinition<?>> knownComponents() {
        return components.keySet();
    }

    public IntSet getEntities() {
        return entities;
    }

    @SuppressWarnings("unchecked")
    private <T> Map<Integer, T> getComponentMap(ComponentDefinition<T> component) {
        return (Map<Integer, T>) components.computeIfAbsent(component, x -> new HashMap<>());
    }
}
