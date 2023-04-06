package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetCurrentCastsPerTurnOnEndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ResetCurrentCastsPerTurnOnEndTurnHandler.class);

    @Override
    public void handle(EndTurnEvent event, NetworkRandom random) {
        LOG.info("Resetting current casts per turn at end of turn");
        for (int entity : data.query(Components.Spell.CURRENT_CASTS_PER_TURN).list()) {
            data.removeComponent(entity, Components.Spell.CURRENT_CASTS_PER_TURN);
        }
    }
}
