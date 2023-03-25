package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.EffectTriggerUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerOnDeathHandler extends GameEventHandler<DestructionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerOnDeathHandler.class);

    @Override
    public void handle(DestructionEvent event, NetworkRandom random) {
        LOG.info("Checking death triggers for death of {}", event.target);
        for (int entity : data.query(Components.DEATH_EFFECT_TRIGGERS).list()) {
            EffectTriggerUtil.trigger(data.getComponent(entity, Components.DEATH_EFFECT_TRIGGERS), entity, new int[] { event.target }, events, random);
        }
    }
}
