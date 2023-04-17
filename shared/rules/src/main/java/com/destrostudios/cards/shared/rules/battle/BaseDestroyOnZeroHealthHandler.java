package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDestroyOnZeroHealthHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDestroyOnZeroHealthHandler.class);

    protected void destroyOnZeroHealth(GameContext context, int entity, T event) {
        EntityData data = context.getData();
        if (StatsUtil.getEffectiveHealth(data, entity) <= 0) {
            LOG.debug("{} has zero or less health (event = {})", inspect(data, entity), event);
            context.getEvents().fire(new DestructionEvent(entity));
        }
    }
}
