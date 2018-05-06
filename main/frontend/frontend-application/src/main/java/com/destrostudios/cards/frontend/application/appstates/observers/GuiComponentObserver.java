package com.destrostudios.cards.frontend.application.appstates.observers;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.ComponentObserver;

public abstract class GuiComponentObserver<T> implements ComponentObserver<T> {

    public GuiComponentObserver(ComponentDefinition<T> component) {
        this.component = component;
    }
    private ComponentDefinition<T> component;

    public ComponentDefinition<T> getComponent() {
        return component;
    }
}
