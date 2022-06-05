package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.function.IntPredicate;

public abstract class GameEventHandler<T extends Event> {

    public EntityData data;
    public EventQueue events;

    public abstract void handle(T event, NetworkRandom random);

    protected <T> IntPredicate hasComponentValue(ComponentDefinition<T> component, T value) {
        return x -> data.hasComponentValue(x, component, value);
    }

    protected IntPredicate hasComponent(ComponentDefinition<?> component) {
        return x -> data.hasComponent(x, component);
    }
}
