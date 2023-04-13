package com.destrostudios.cards.shared.entities;

import java.util.Optional;

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

    default <T> Optional<T> getOptionalComponent(int entity, ComponentDefinition<T> component) {
        return hasComponent(entity, component) ? Optional.of(getComponent(entity, component)) : Optional.empty();
    }

    default void setComponent(int entity, ComponentDefinition<Void> component) {
        setComponent(entity, component, null);
    }

    Aggregator query(ComponentDefinition<?> component);

    Aggregator queryAll(ComponentDefinition<?>... component);
}
