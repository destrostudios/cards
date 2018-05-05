package com.destrostudios.cards.shared.entities;

/**
 *
 * @author Philipp
 */
public class ComponentDefinition<T> {

    private final String name;

    public ComponentDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
