package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.cards.backend.application.services.UserService;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.game.ServerGameData;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.destrostudios.gametools.network.shared.modules.game.messages.GameActionRequest;
import com.esotericsoftware.kryonet.Connection;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class GameOverModule extends NetworkModule {

    private static final Logger LOG = LoggerFactory.getLogger(QueueServerModule.class);

    private GameServerModule<GameContext, Event> gameModule;
    private UserService userService;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameActionRequest message) {
            ServerGameData<GameContext> game = gameModule.getGame(message.game());
            if (game.state.isGameOver()) {
                gameModule.unregisterGame(game.id);
                LOG.info("Unlisted and unregistered game \"" + game.id + "\".");
                int winnerUserId = game.state.getUserId(game.state.getWinner());
                if (winnerUserId != QueueServerModule.USER_ID_BOT) {
                    userService.addPacks(winnerUserId, GameConstants.PACKS_FOR_WINNER);
                }
            }
        }
    }
}
