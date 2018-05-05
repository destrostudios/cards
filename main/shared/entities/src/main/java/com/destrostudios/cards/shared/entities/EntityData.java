package com.destrostudios.cards.shared.entities;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import java.util.Objects;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public interface EntityData {

    int createEntity();

    boolean hasComponent(int entity, ComponentDefinition<?> component);

    <T> T getComponent(int entity, ComponentDefinition<T> component);

    <T> void setComponent(int entity, ComponentDefinition<T> component, T value);

    <T> void removeComponent(int entity, ComponentDefinition<T> component);

    IntArrayList entities(ComponentDefinition<?> component, IntPredicate... predicates);

    default <T> T getComponentOrDefault(int entity, ComponentDefinition<T> component, T defaultValue) {
        return hasComponent(entity, component) ? getComponent(entity, component) : defaultValue;
    }

    default void setComponent(int entity, ComponentDefinition<Void> component) {
        setComponent(entity, component, null);
    }

    default <T> boolean hasComponentValue(int entity, ComponentDefinition<T> component, T value) {
        return hasComponent(entity, component) && Objects.equals(getComponent(entity, component), value);
    }

    default int entity(ComponentDefinition<?> component, IntPredicate... predicates) {
        IntArrayList result = entities(component, predicates);
        if (result.size() != 1) {
            throw new IllegalStateException(Integer.toString(result.size()));
        }
        return result.get(0);
    }
}
