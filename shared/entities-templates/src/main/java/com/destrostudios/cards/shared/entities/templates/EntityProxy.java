package com.destrostudios.cards.shared.entities.templates;

public record EntityProxy(int entity) {

    @Override
    public String toString() {
        return Integer.toString(entity);
    }
}
