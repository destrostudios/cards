package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrawCardOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DrawCardOnTurnStartHandler.class);

    @Override
    public void handle(StartTurnEvent event, NetworkRandom random) {
        LOG.debug("Player {} is drawing card at start of turn", inspect(event.player));
        events.fire(new DrawCardEvent(event.player), random);
    }
}
