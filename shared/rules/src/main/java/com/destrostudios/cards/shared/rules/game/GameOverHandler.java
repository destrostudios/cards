package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class GameOverHandler extends GameEventHandler<GameOverEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(GameOverHandler.class);

    private GameContext gameContext;

    @Override
    public void handle(GameOverEvent event, NetworkRandom random) {
        LOG.debug("Game over, winner = {}", inspect(event.winner));
        gameContext.onGameOver(event.winner);
    }
}
