package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.game.LobbyServerModule;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.destrostudios.gametools.network.shared.modules.jwt.messages.Login;
import com.esotericsoftware.kryonet.Connection;

import java.util.Map;
import java.util.UUID;

public class AutoRejoinModule extends NetworkModule {

    private final JwtServerModule jwtModule;
    private final LobbyServerModule<StartGameInfo> lobbyModule;
    private final GameServerModule<GameContext, Event> gameModule;

    public AutoRejoinModule(JwtServerModule jwtModule, LobbyServerModule<StartGameInfo> lobbyModule, GameServerModule<GameContext, Event> gameModule) {
        this.jwtModule = jwtModule;
        this.lobbyModule = lobbyModule;
        this.gameModule = gameModule;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Login) {
            JwtAuthenticationUser user = jwtModule.getUser(connection.getID());
            if (user == null) {
                // unsuccessful login
                return;
            }
            for (Map.Entry<UUID, StartGameInfo> entry : lobbyModule.getGames().entrySet()) {
                StartGameInfo startGameInfo = entry.getValue();
                if (ModuleUtil.isUserInGame(startGameInfo, user)) {
                    gameModule.join(connection, entry.getKey());
                }
            }
        }
    }
}
