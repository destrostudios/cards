package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerOnDamageHandler extends GameEventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerOnDamageHandler.class);

    @Override
    public void handle(DamageEvent event, NetworkRandom random) {
        LOG.info("Checking damage triggers for {}", event.target);
        for (int entity : data.query(Components.DAMAGE_TRIGGERS).list()) {
            TriggerUtil.trigger(data.getComponent(entity, Components.DAMAGE_TRIGGERS), entity, new int[] { event.target }, events, random);
        }
    }
}
