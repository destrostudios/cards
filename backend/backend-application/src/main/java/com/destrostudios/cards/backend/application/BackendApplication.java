package com.destrostudios.cards.backend.application;

import amara.libraries.database.Database;
import amara.libraries.database.databases.MySQLDatabase;
import com.destrostudios.authtoken.NoValidateJwtService;
import com.destrostudios.cards.backend.application.modules.*;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotModule;
import com.destrostudios.cards.backend.application.services.*;
import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.network.NetworkUtil;
import com.destrostudios.cards.shared.rules.CardsNetworkService;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.network.server.ToolsServer;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.Date;

public class BackendApplication {

    public static void main(String[] args) throws IOException {
        ApplicationSetup.setup();

        Database database = getDatabase();
        ModeService modeService = new ModeService(database);
        CardService cardService = new CardService(database);
        FoilService foilService = new FoilService(database);
        CardListService cardListService = new CardListService(database, cardService, foilService);
        UserService userService = new UserService(database, modeService, cardService, foilService, cardListService);

        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        // Log.DEBUG();
        Log.info(new Date().toString()); // time reference for kryo logs

        Server kryoServer = new Server(10_000_000, 10_000_000);
        NetworkUtil.setupSerializer(kryoServer.getKryo());

        System.err.println("WARNING: Using jwt service without validation.");
        JwtServerModule jwtModule = new JwtServerModule(new NoValidateJwtService(), kryoServer::getConnections);
        GameDataServerModule gameDataServerModule = new GameDataServerModule(jwtModule, modeService, cardService, userService);
        GameServerModule<GameContext, Event> gameModule = new GameServerModule<>(new CardsNetworkService(true), kryoServer::getConnections);
        CardsBotModule cardsBotModule = new CardsBotModule(gameModule);
        CardsGameStartServerModule gameStartModule = new CardsGameStartServerModule(kryo -> {}, kryoServer, jwtModule, gameModule, cardService);
        QueueServerModule queueModule = new QueueServerModule(jwtModule, gameStartModule, cardsBotModule, userService);

        GameOverModule gameOverModule = new GameOverModule(gameModule);
        AutoRejoinModule autoRejoinModule = new AutoRejoinModule(jwtModule, gameModule);

        ToolsServer server = new ToolsServer(kryoServer, jwtModule, gameDataServerModule, gameModule, cardsBotModule, gameStartModule, queueModule, gameOverModule, autoRejoinModule);
        server.start(NetworkUtil.PORT);

        System.out.println("Server started.");
    }

    public static Database getDatabase() {
        return new MySQLDatabase("//localhost:3306/cards", "root", "");
    }
}
