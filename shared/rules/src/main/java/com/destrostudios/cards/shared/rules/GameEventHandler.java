package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.util.DebugUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public abstract class GameEventHandler<E extends Event> implements EventHandler<E, GameContext> {

    protected GameContext context;
    protected EntityData data;
    protected EventQueue events;

    @Override
    public void onEvent(E event, GameContext context) {
        this.context = context;
        data = context.getData();
        events = context.getEvents();
        handle(event, context.getRandom());
    }

    public abstract void handle(E event, NetworkRandom random);

    protected Object inspect(Iterable<Integer> entities) {
        return new DebugUtil.EntityDebugText_Iterable(data, entities);
    }

    protected Object inspect(int... entities) {
        return new DebugUtil.EntityDebugText_Array(data, entities);
    }
}
