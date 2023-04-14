package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDestroyOnZeroHealthHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDestroyOnZeroHealthHandler.class);

    @Override
    public void handle(T event, NetworkRandom random) {
        for (int target : getAffectedTargets(data, event)) {
            if (StatsUtil.getEffectiveHealth(data, target) <= 0) {
                LOG.debug("{} has zero or less health (event = {})", inspect(target), event);
                events.fire(new DestructionEvent(target), random);
            }
        }
    }

    protected abstract IntList getAffectedTargets(EntityData data, T event);
}