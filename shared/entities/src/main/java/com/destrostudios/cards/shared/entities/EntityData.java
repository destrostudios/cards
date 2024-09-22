package com.destrostudios.cards.shared.entities;

import java.util.function.IntPredicate;

public interface EntityData {

    int createEntity();

    boolean hasComponent(int entity, ComponentDefinition<?> component);

    <T> T getComponent(int entity, ComponentDefinition<T> component);

    <T> T getComponentOrElse(int entity, ComponentDefinition<T> component, T defaultValue);

    <T> void setComponent(int entity, ComponentDefinition<T> component, T value);

    <T> void removeComponent(int entity, ComponentDefinition<T> component);

    default void setComponent(int entity, ComponentDefinition<Void> component) {
        setComponent(entity, component, null);
    }

    IntList list(ComponentDefinition<?> component);

    IntList list(ComponentDefinition<?> component, IntPredicate predicate);

    IntList listAll(ComponentDefinition<?>... components);

    IntList listAll(ComponentDefinition<?>[] components, IntPredicate predicate);

    int count(ComponentDefinition<?> component);

    int count(ComponentDefinition<?> component, IntPredicate predicate);

    int unique(ComponentDefinition<?> component);
}
