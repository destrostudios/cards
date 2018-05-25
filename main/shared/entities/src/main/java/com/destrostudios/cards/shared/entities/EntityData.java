package com.destrostudios.cards.shared.entities;

import java.util.Objects;
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

    default <T> T getComponentOrDefault(int entity, ComponentDefinition<T> component, T defaultValue) {
        return getOptionalComponent(entity, component).orElse(defaultValue);
    }

    default <T> Optional<T> getOptionalComponent(int entity, ComponentDefinition<T> component) {
        return hasComponent(entity, component) ? Optional.of(getComponent(entity, component)) : Optional.empty();
    }

    default void setComponent(int entity, ComponentDefinition<Void> component) {
        setComponent(entity, component, null);
    }

    default <T> boolean hasComponentValue(int entity, ComponentDefinition<T> component, T value) {
        return hasComponent(entity, component) && Objects.equals(getComponent(entity, component), value);
    }

    <T> Aggregator<T> query(ComponentDefinition<T> component);
}
