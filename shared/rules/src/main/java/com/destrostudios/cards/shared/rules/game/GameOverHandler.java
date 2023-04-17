package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameOverHandler extends GameEventHandler<GameOverEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(GameOverHandler.class);

    @Override
    public void handle(GameContext context, GameOverEvent event) {
        LOG.debug("Game over, winner = {}", inspect(context.getData(), event.winner));
        context.onGameOver(event.winner);
    }
}
