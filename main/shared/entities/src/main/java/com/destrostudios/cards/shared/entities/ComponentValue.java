package com.destrostudios.cards.shared.entities;

/**
 *
 * @author Philipp
 */
public class ComponentValue {

    private int entity, componentValue;

    ComponentValue() {
    }

    ComponentValue(int entity, int componentValue) {
        this.entity = entity;
        this.componentValue = componentValue;
    }

    public int getEntity() {
        return entity;
    }

    void setEntity(int entity) {
        this.entity = entity;
    }

    public int getComponentValue() {
        return componentValue;
    }

    void setComponentValue(int componentValue) {
        this.componentValue = componentValue;
    }
}
