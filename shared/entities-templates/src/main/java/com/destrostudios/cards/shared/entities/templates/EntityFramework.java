package com.destrostudios.cards.shared.entities.templates;

public interface EntityFramework<COMPONENT> {

    int createEntity();

    <T> void setComponent(int entity, COMPONENT component, T value);
}
