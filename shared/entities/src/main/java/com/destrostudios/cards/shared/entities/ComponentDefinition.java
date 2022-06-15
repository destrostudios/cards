package com.destrostudios.cards.shared.entities;

import java.util.Objects;

public class ComponentDefinition<T> {

    private int id;
    private String name;

    private ComponentDefinition() {
        // Used by serializer
    }

    public ComponentDefinition(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ComponentDefinition{id=" + id + ",name=" + name + '}';
    }

    // Autogenerated, needed so that deserialized ComponentDefinitions match the "real" ones in Components

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentDefinition<?> that = (ComponentDefinition<?>) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
