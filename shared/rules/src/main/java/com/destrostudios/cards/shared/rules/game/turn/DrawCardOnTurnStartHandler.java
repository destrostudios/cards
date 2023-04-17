package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrawCardOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DrawCardOnTurnStartHandler.class);

    @Override
    public void handle(GameContext context, StartTurnEvent event) {
        LOG.debug("Player {} is drawing card at start of turn", inspect(context.getData(), event.player));
        context.getEvents().fire(new DrawCardEvent(event.player));
    }
}
