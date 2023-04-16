package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDestroyOnZeroHealthHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDestroyOnZeroHealthHandler.class);

    protected void destroyOnZeroHealth(int entity, T event, NetworkRandom random) {
        if (StatsUtil.getEffectiveHealth(data, entity) <= 0) {
            LOG.debug("{} has zero or less health (event = {})", inspect(entity), event);
            events.fire(new DestructionEvent(entity), random);
        }
    }
}
