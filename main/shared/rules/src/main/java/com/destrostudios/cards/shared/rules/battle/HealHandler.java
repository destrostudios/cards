package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealHandler extends GameEventHandler<HealEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(HealHandler.class);

    @Override
    public void handle(HealEvent event, NetworkRandom random) {
        LOG.info("healing {} health to {}", event.heal, event.target);
        events.fire(new SetDamagedEvent(event.target, data.getOptionalComponent(event.target, Components.DAMAGED).orElse(0) - event.heal), random);
    }
}
