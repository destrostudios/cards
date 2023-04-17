package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameOverHandler extends GameEventHandler<GameOverEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(GameOverHandler.class);

    @Override
    public void handle(GameOverEvent event, NetworkRandom random) {
        LOG.debug("Game over, winner = {}", inspect(event.winner));
        context.onGameOver(event.winner);
    }
}
