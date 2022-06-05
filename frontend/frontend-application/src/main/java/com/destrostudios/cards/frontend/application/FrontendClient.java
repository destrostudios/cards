package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.network.NetworkUtil;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.KryoStartGameInfo;
import com.destrostudios.cards.shared.rules.NetworkCardsService;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.gametools.network.client.ToolsClient;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.game.GameStartClientModule;
import com.destrostudios.gametools.network.client.modules.game.LobbyClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import com.esotericsoftware.kryonet.Client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class FrontendClient {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("First argument must be a jwt (usually passed by the destrostudios launcher).");
            return;
        }
        String hostUrl = (args.length > 1 ? args[1] : "destrostudios.com");
        startApplication(hostUrl, args[0]);
    }

    private static void startApplication(String hostUrl, String jwt) throws IOException {
        try {
            FileOutputStream logFileOutputStream = new FileOutputStream("./log.txt");
            System.setOut(new PrintStream(new MultipleOutputStream(System.out, logFileOutputStream)));
            System.setErr(new PrintStream(new MultipleOutputStream(System.err, logFileOutputStream)));
        } catch (FileNotFoundException ex) {
            System.err.println("Error while accessing log file: " + ex.getMessage());
        }
        ApplicationSetup.setup();
        // Log.DEBUG();
        ToolsClient toolsClient = getToolsClient(hostUrl, jwt);
        FrontendJmeApplication frontendJmeApplication = new FrontendJmeApplication(toolsClient);
        frontendJmeApplication.start();
    }

    private static ToolsClient getToolsClient(String hostUrl, String jwt) throws IOException {
        Client kryoClient = new Client(10_000_000, 10_000_000);

        JwtClientModule jwtModule = new JwtClientModule(kryoClient);
        GameClientModule<GameContext, Event> gameModule = new GameClientModule<>(new NetworkCardsService(false), kryoClient);
        LobbyClientModule<StartGameInfo> lobbyModule = new LobbyClientModule<>(KryoStartGameInfo::initialize, kryoClient);
        GameStartClientModule<StartGameInfo> gameStartModule = new GameStartClientModule<>(KryoStartGameInfo::initialize, kryoClient);

        ToolsClient client = new ToolsClient(kryoClient, jwtModule, gameModule, lobbyModule, gameStartModule);
        client.start(10_000, hostUrl, NetworkUtil.PORT);

        jwtModule.login(jwt);
        lobbyModule.subscribeToGamesList();

        return client;
    }
}