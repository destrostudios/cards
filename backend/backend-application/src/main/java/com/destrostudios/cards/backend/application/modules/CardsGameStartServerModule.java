package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.services.CardService;
import com.destrostudios.cards.shared.rules.*;
import com.destrostudios.cards.shared.rules.actions.Action;
import com.destrostudios.cards.shared.rules.actions.GameStartAction;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.game.GameStartServerModule;
import com.destrostudios.gametools.network.server.modules.game.ServerGameData;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class CardsGameStartServerModule extends GameStartServerModule<StartGameInfo> {

    public CardsGameStartServerModule(Server kryoServer, JwtServerModule jwtModule, GameServerModule<GameContext, Action> gameModule, CardService cardService) {
        super(_ -> {});
        this.kryoServer = kryoServer;
        this.jwtModule = jwtModule;
        this.gameModule = gameModule;
        this.cardService = cardService;
    }
    private Server kryoServer;
    private JwtServerModule jwtModule;
    private GameServerModule<GameContext, Action> gameModule;
    private CardService cardService;

    @Override
    public void startGameRequest(Connection connection, StartGameInfo startGameInfo) {
        startGame(startGameInfo);
    }

    public UUID startGame(StartGameInfo startGameInfo) {
        GameContext gameContext = new GameContext(startGameInfo, GameEventHandling.GLOBAL_INSTANCE);
        GameSetup.apply(gameContext.getData(), gameContext.getStartGameInfo(), cardService.getCards(), new Random());

        UUID gameId = UUID.randomUUID();
        System.out.println("Start game \"" + gameId + "\".");
        gameModule.registerGame(new ServerGameData<>(gameId, gameContext, new SecureRandom()));
        gameModule.applyAction(gameId, new GameStartAction());

        for (Connection other : kryoServer.getConnections()) {
            JwtAuthenticationUser user = jwtModule.getUser(other.getID());
            if (user != null && ModuleUtil.isUserInGame(startGameInfo, user)) {
                gameModule.join(other, gameId);
            }
        }
        return gameId;
    }
}
