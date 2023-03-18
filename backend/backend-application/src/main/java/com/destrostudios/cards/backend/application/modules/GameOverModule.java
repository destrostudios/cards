package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.cards.backend.application.services.UserService;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.GameContext;
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
        int winnerUserId = gameContext.getUserId(gameContext.getWinner());
        // TODO: Extract logic
        if (winnerUserId != QueueServerModule.USER_ID_BOT) {
            userService.addPacks(winnerUserId, mode.getId(), GameConstants.PACKS_FOR_WINNER);
        }
        // TODO: Extract and cleanup logic
        if (mode.getName().equals(GameConstants.MODE_NAME_ARENA)) {
            int loserUserId = ((winnerUserId == startGameInfo.getPlayer1().getId()) ? startGameInfo.getPlayer2().getId() : startGameInfo.getPlayer1().getId());
            if (loserUserId != QueueServerModule.USER_ID_BOT) {
                userService.deleteAllUserModeDecksAndCollectionCards(loserUserId, mode.getId());
                userService.setPacks(loserUserId, mode.getId(), GameConstants.PACKS_FOR_NEW_ARENA_RUN);
            }
        }
    }
}
