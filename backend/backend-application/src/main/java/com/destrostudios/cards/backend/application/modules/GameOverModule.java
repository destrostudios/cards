package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.game.ServerGameData;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.destrostudios.gametools.network.shared.modules.game.messages.GameActionRequest;
import com.esotericsoftware.kryonet.Connection;

public class GameOverModule extends NetworkModule {

    private final GameServerModule<GameContext, Event> gameModule;

    public GameOverModule(GameServerModule<GameContext, Event> gameModule) {
        this.gameModule = gameModule;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameActionRequest message) {
            ServerGameData<GameContext> game = gameModule.getGame(message.game());
            if (game.state.isGameOver()) {
                gameModule.unregisterGame(game.id);
                System.out.println("Unlisted and unregistered game \"" + game.id + "\".");
            }
        }
    }
}
