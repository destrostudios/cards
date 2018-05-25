package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventQueue;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public abstract class GameEventHandler<T extends Event> {

    public EntityData data;
    public EventQueue events;
    public IntUnaryOperator random;

    public abstract void handle(T event);
}
