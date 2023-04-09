package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndTurnHandler.class);

    @Override
    public void handle(EndTurnEvent event, NetworkRandom random) {
        LOG.debug("Ending turn of player {}", inspect(event.player));
        data.removeComponent(event.player, Components.Game.ACTIVE_PLAYER);
        int nextPlayer = data.getComponent(event.player, Components.NEXT_PLAYER);
        events.fire(new StartTurnEvent(nextPlayer), random);
    }
}
