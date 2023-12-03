package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.backend.application.services.UserService;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
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
    private Database database;
    private UserService userService;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameActionRequest message) {
            ServerGameData<GameContext> game = gameModule.getGame(message.game());
            if (game.state.isGameOver()) {
                gameModule.unregisterGame(game.id);
                LOG.info("Unregistered game \"" + game.id + "\".");
                onGameOver(game.state);
            }
        }
    }

    private void onGameOver(GameContext gameContext) {
        StartGameInfo startGameInfo = gameContext.getStartGameInfo();
        Mode mode = startGameInfo.getMode();
        Queue queue = startGameInfo.getQueue();
        int winnerUserId = gameContext.getUserId(gameContext.getWinner());
        int loserUserId = ((winnerUserId == startGameInfo.getPlayers()[0].getId()) ? startGameInfo.getPlayers()[1].getId() : startGameInfo.getPlayers()[0].getId());
        database.transaction(() -> {
            for (PlayerInfo playerInfo : startGameInfo.getPlayers()) {
                if (playerInfo.getId() != QueueServerModule.BOT_USER_ID) {
                    boolean win = (playerInfo.getId() == winnerUserId);
                    userService.onGameOver(playerInfo.getId(), mode.getId(), queue.getId(), win);
                }
            }
            // TODO: Extract mode logic below?
            switch (mode.getName()) {
                case GameConstants.MODE_NAME_CLASSIC: {
                    if (winnerUserId != QueueServerModule.BOT_USER_ID) {
                        userService.addPacks(winnerUserId, GameConstants.PACKS_FOR_WINNER);
                    }
                }
                case GameConstants.MODE_NAME_ARENA: {
                    if (loserUserId != QueueServerModule.BOT_USER_ID) {
                        userService.onArenaLoss(loserUserId);
                    }
                }
            }
        });
    }
}
