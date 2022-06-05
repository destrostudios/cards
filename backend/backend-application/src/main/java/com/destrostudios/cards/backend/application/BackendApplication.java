package com.destrostudios.cards.backend.application;

import com.destrostudios.authtoken.NoValidateJwtService;
import com.destrostudios.cards.backend.application.modules.AutoRejoinModule;
import com.destrostudios.cards.backend.application.modules.GameOverModule;
import com.destrostudios.cards.backend.application.modules.CardsGameStartServerModule;
import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.network.NetworkUtil;
import com.destrostudios.cards.shared.rules.NetworkCardsService;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.KryoStartGameInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.gametools.network.server.ToolsServer;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.game.GameStartServerModule;
import com.destrostudios.gametools.network.server.modules.game.LobbyServerModule;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.Date;

public class BackendApplication {

    public static void main(String[] args) throws IOException {
        ApplicationSetup.setup();

        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        // Log.DEBUG();
        Log.info(new Date().toString()); // time reference for kryo logs

        Server kryoServer = new Server(10_000_000, 10_000_000);
        System.err.println("WARNING: Using jwt service without validation.");
        JwtServerModule jwtModule = new JwtServerModule(new NoValidateJwtService(), kryoServer::getConnections);
        GameServerModule<GameContext, Event> gameModule = new GameServerModule<>(new NetworkCardsService(true), kryoServer::getConnections);
        LobbyServerModule<StartGameInfo> lobbyModule = new LobbyServerModule<>(KryoStartGameInfo::initialize, kryoServer::getConnections);
        GameStartServerModule<StartGameInfo> cardsStartModule = new CardsGameStartServerModule(KryoStartGameInfo::initialize, kryoServer, jwtModule, gameModule, lobbyModule);

        GameOverModule gameOverModule = new GameOverModule(lobbyModule, gameModule);
        AutoRejoinModule autoRejoinModule = new AutoRejoinModule(jwtModule, lobbyModule, gameModule);

        ToolsServer server = new ToolsServer(kryoServer, jwtModule, gameModule, lobbyModule, cardsStartModule, gameOverModule, autoRejoinModule);
        server.start(NetworkUtil.PORT);

        System.out.println("Server started.");
    }
}
