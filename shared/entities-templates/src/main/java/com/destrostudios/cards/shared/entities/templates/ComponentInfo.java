package com.destrostudios.cards.shared.entities.templates;

public interface ComponentInfo<COMPONENT> {

    int getId(COMPONENT component);

    String getName(COMPONENT component);
}
