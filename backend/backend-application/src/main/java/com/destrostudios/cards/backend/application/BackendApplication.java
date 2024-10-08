package com.destrostudios.cards.backend.application;

import com.destrostudios.authtoken.NoValidateJwtService;
import com.destrostudios.cards.backend.application.modules.*;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotModule;
import com.destrostudios.cards.backend.application.services.*;
import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.backend.database.databases.databases.MySQLDatabase;
import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.files.FileManager;
import com.destrostudios.cards.shared.network.NetworkUtil;
import com.destrostudios.cards.shared.rules.CardsNetworkService;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.actions.Action;
import com.destrostudios.gametools.network.server.ToolsServer;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class BackendApplication {

    public static void main(String[] args) throws IOException {
        ApplicationSetup.setup();

        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        // Log.DEBUG();
        Log.info(new Date().toString()); // Time reference for kryo logs

        Database database = getDatabase();
        CardService cardService = new CardService(database);
        FoilService foilService = new FoilService(database);
        CardListService cardListService = new CardListService(database, cardService, foilService);
        ModeService modeService = new ModeService(database, cardListService);
        QueueService queueService = new QueueService(database);
        ArenaService arenaService = new ArenaService(cardService);
        EntityService entityService = new EntityService(cardService);
        PackService packService = new PackService(cardService, foilService, entityService);
        UserService userService = new UserService(database, modeService, cardService, foilService, cardListService, queueService, arenaService, packService);
        DeckService deckService = new DeckService(modeService, userService);

        BotDeckService botDeckService = new BotDeckService(database, cardService, foilService, cardListService, modeService, queueService, entityService);
        // botDeckService.generateDecks(123);

        Server kryoServer = new Server(10_000_000, 10_000_000);
        NetworkUtil.setupSerializer(kryoServer.getKryo());

        System.err.println("WARNING: Using jwt service without validation.");
        JwtServerModule jwtModule = new JwtServerModule(new NoValidateJwtService(), kryoServer::getConnections);
        GameDataServerModule gameDataServerModule = new GameDataServerModule(jwtModule, database, cardService, modeService, queueService, userService, deckService);
        GameServerModule<GameContext, Action> gameModule = new GameServerModule<>(new CardsNetworkService(true), kryoServer::getConnections);
        CardsBotModule cardsBotModule = new CardsBotModule(gameModule);
        CardsGameStartServerModule gameStartModule = new CardsGameStartServerModule(kryoServer, jwtModule, gameModule, cardService);
        QueueServerModule queueModule = new QueueServerModule(jwtModule, gameStartModule, cardsBotModule, modeService, deckService, queueService);
        GameOverModule gameOverModule = new GameOverModule(gameModule, database, userService);
        AutoRejoinModule autoRejoinModule = new AutoRejoinModule(jwtModule, gameModule);

        ToolsServer server = new ToolsServer(kryoServer, jwtModule, gameDataServerModule, gameModule, cardsBotModule, gameStartModule, queueModule, gameOverModule, autoRejoinModule);
        server.start(NetworkUtil.PORT);

        System.out.println("Server started.");
    }

    public static Database getDatabase() {
        List<String> databaseSecrets = FileManager.getFileLines("./database.ini");
        return new MySQLDatabase(databaseSecrets.get(0), databaseSecrets.get(1), databaseSecrets.get(2));
    }
}
