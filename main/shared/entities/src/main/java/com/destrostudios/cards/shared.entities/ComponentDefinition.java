package com.destrostudios.cards.shared.entities;

/**
 *
 * @author Philipp
 */
public class ComponentDefinition<T> {

    private final String name;
    private final int key;
    private final ValueMapping<T> mapping;

    public ComponentDefinition(String name, int key, ValueMapping<T> mapping) {
        this.name = name;
        this.key = key;
        this.mapping = mapping;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public ValueMapping<T> getMapping() {
        return mapping;
    }
}