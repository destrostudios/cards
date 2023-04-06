package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestroyOnZeroHealthHandler extends GameEventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DestroyOnZeroHealthHandler.class);

    @Override
    public void handle(DamageEvent event, NetworkRandom random) {
        if (StatsUtil.getEffectiveHealth(data, event.target) <= 0) {
            LOG.info(inspect(event.target) + " has zero or less health");
            events.fire(new DestructionEvent(event.target), random);
        }
    }
}
