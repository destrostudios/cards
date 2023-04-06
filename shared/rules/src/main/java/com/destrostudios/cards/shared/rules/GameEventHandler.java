package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.util.DebugUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public abstract class GameEventHandler<T extends Event> {

    public EntityData data;
    public EventQueue events;

    public abstract void handle(T event, NetworkRandom random);

    protected String inspect(Iterable<Integer> entities) {
        return DebugUtil.getDebugText(data, entities);
    }

    protected String inspect(int... entities) {
        return DebugUtil.getDebugText(data, entities);
    }
}
