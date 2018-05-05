package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventQueue;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public interface GameEventHandler<T extends Event> {

    void handle(EntityData data, EventQueue events, IntUnaryOperator random, T event);
}
