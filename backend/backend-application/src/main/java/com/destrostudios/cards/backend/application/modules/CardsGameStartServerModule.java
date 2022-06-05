package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.TestGameSetup;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.game.GameStartServerModule;
import com.destrostudios.gametools.network.server.modules.game.LobbyServerModule;
import com.destrostudios.gametools.network.server.modules.game.ServerGameData;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.function.Consumer;

public class CardsGameStartServerModule extends GameStartServerModule<StartGameInfo> {

    private final Server kryoServer;
    private final JwtServerModule jwtModule;
    private final GameServerModule<GameContext, Event> gameModule;
    private final LobbyServerModule<StartGameInfo> lobbyModule;

    public CardsGameStartServerModule(Consumer<Kryo> registerParams, Server kryoServer, JwtServerModule jwtModule, GameServerModule<GameContext, Event> gameModule, LobbyServerModule<StartGameInfo> lobbyModule) {
        super(registerParams);
        this.kryoServer = kryoServer;
        this.jwtModule = jwtModule;
        this.gameModule = gameModule;
        this.lobbyModule = lobbyModule;
    }

    @Override
    public void startGameRequest(Connection connection, StartGameInfo startGameInfo) {
        SimpleEntityData data = new SimpleEntityData();
        TestGameSetup testGameSetup = new TestGameSetup(data, startGameInfo);
        testGameSetup.apply();
        GameContext gameContext = new GameContext(data);

        UUID gameId = UUID.randomUUID();
        lobbyModule.listGame(gameId, startGameInfo);
        System.out.println("Game \"" + gameId + "\" listed.");
        gameModule.registerGame(new ServerGameData<>(gameId, gameContext, new SecureRandom()));
        gameModule.applyAction(gameId, new GameStartEvent());

        for (Connection other : kryoServer.getConnections()) {
            JwtAuthenticationUser user = jwtModule.getUser(other.getID());
            if (user != null && ModuleUtil.isUserInGame(startGameInfo, user)) {
                gameModule.join(other, gameId);
            }
        }
    }
}
