package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.rules.util.DebugUtil;

public abstract class GameEventHandler<E extends Event> implements EventHandler<E, GameContext> {

    protected static Object inspect(EntityData data, Iterable<Integer> entities) {
        return new DebugUtil.EntityDebugText_Iterable(data, entities);
    }

    protected static Object inspect(EntityData data, int... entities) {
        return new DebugUtil.EntityDebugText_Array(data, entities);
    }
}
