package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotModule;
import com.destrostudios.cards.backend.application.services.UserService;
import com.destrostudios.cards.shared.model.UserCardList;
import com.destrostudios.cards.shared.network.messages.QueueMessage;
import com.destrostudios.cards.shared.network.messages.UnqueueMessage;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.destrostudios.gametools.network.shared.modules.jwt.messages.Logout;
import com.esotericsoftware.kryonet.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class QueueServerModule extends NetworkModule {

    private static final Logger LOG = LoggerFactory.getLogger(QueueServerModule.class);

    public QueueServerModule(JwtServerModule jwtModule, CardsGameStartServerModule cardsGameStartServerModule, CardsBotModule cardsBotModule, UserService userService) {
        this.jwtModule = jwtModule;
        this.cardsGameStartServerModule = cardsGameStartServerModule;
        this.cardsBotModule = cardsBotModule;
        this.userService = userService;
        playersInQueue = new HashMap<>();
    }
    public static final int USER_ID_BOT = 0;
    private JwtServerModule jwtModule;
    private CardsGameStartServerModule cardsGameStartServerModule;
    private CardsBotModule cardsBotModule;
    private UserService userService;
    private HashMap<Integer, PlayerInfo> playersInQueue;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof QueueMessage queueMessage) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            // Successful login
            if (jwtUser != null) {
                int userId = (int) jwtUser.id;
                LOG.info(jwtUser.login + " queued up (againstHumanOrBot = " + queueMessage.isAgainstHumanOrBot() + ", cardListId = " + queueMessage.getUserCardListId() + ").");
                UserCardList userCardList = userService.getUserCardList(queueMessage.getUserCardListId());
                PlayerInfo playerInfo = new PlayerInfo(userId, jwtUser.login, userCardList);
                if (queueMessage.isAgainstHumanOrBot()) {
                    playersInQueue.put(userId, playerInfo);
                    startGameIfPossible();
                } else {
                    UUID gameId = cardsGameStartServerModule.startGame(new StartGameInfo("forest", playerInfo, new PlayerInfo(USER_ID_BOT, "Bot", null)));
                    cardsBotModule.checkBotTurn(gameId);
                }
            }
        } else if ((object instanceof UnqueueMessage) || (object instanceof Logout)) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            // Successful login
            if (jwtUser != null) {
                LOG.info(jwtUser.login + " unqueued.");
                playersInQueue.remove((int) jwtUser.id);
            }
        }
    }

    private void startGameIfPossible() {
        if (playersInQueue.size() > 1) {
            PlayerInfo playerInfo1 = popPlayerInfo();
            PlayerInfo playerInfo2 = popPlayerInfo();
            cardsGameStartServerModule.startGame(new StartGameInfo("forest", playerInfo1, playerInfo2));
        }
    }

    private PlayerInfo popPlayerInfo() {
        int playerId = playersInQueue.keySet().iterator().next();
        return playersInQueue.remove(playerId);
    }
}
