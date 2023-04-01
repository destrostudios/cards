package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerOnHealHandler extends GameEventHandler<HealEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerOnHealHandler.class);

    @Override
    public void handle(HealEvent event, NetworkRandom random) {
        LOG.info("Checking heal triggers for {}", event.target);
        for (int entity : data.query(Components.HEAL_TRIGGERS).list()) {
            TriggerUtil.trigger(data.getComponent(entity, Components.HEAL_TRIGGERS), entity, new int[] { event.target }, events, random);
        }
    }
}
