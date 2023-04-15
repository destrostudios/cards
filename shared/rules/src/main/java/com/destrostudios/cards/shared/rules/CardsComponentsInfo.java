package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.ComponentInfo;

public class CardsComponentsInfo implements ComponentInfo<ComponentDefinition<?>> {

    @Override
    public int getId(ComponentDefinition<?> component) {
        return component.getId();
    }

    @Override
    public String getName(ComponentDefinition<?> component) {
        return component.getName();
    }
}
