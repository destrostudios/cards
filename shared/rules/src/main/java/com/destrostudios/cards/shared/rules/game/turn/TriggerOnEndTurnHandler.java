package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerOnEndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerOnEndTurnHandler.class);

    @Override
    public void handle(EndTurnEvent event, NetworkRandom random) {
        LOG.info("Checking turn end triggers for {}", event.player);
        for (int entity : data.query(Components.TURN_END_TRIGGERS).list()) {
            TriggerUtil.trigger(data.getComponent(entity, Components.TURN_END_TRIGGERS), entity, new int[] { event.player }, events, random);
        }
    }
}
