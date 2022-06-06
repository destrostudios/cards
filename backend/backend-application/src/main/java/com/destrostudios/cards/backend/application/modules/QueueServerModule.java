package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.shared.network.modules.QueueMessage;
import com.destrostudios.cards.shared.network.modules.QueueModule;
import com.destrostudios.cards.shared.network.modules.UnqueueMessage;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.destrostudios.gametools.network.shared.modules.jwt.messages.Logout;
import com.esotericsoftware.kryonet.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;

public class QueueServerModule extends QueueModule {

    private static final Logger LOG = LoggerFactory.getLogger(QueueServerModule.class);

    public QueueServerModule(JwtServerModule jwtModule, CardsGameStartServerModule cardsGameStartServerModule) {
        this.jwtModule = jwtModule;
        this.cardsGameStartServerModule = cardsGameStartServerModule;
        playersInQueue = new HashMap<>();
    }
    private JwtServerModule jwtModule;
    private CardsGameStartServerModule cardsGameStartServerModule;
    private HashMap<Long, PlayerInfo> playersInQueue;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof QueueMessage queueMessage) {
            JwtAuthenticationUser user = jwtModule.getUser(connection.getID());
            // Successful login
            if (user != null) {
                LOG.info(user.login + " queued up (againstHumanOrBot = " + queueMessage.isAgainstHumanOrBot() + ", library size = " + queueMessage.getLibraryTemplates().size() + ").");
                PlayerInfo playerInfo = new PlayerInfo(user.id, user.login, queueMessage.getLibraryTemplates());
                if (queueMessage.isAgainstHumanOrBot()) {
                    playersInQueue.put(user.id, playerInfo);
                    startGameIfPossible();
                } else {
                    cardsGameStartServerModule.startGame(new StartGameInfo("forest", playerInfo, new PlayerInfo(2, "Bot", new LinkedList<>())));
                }
            }
        } else if ((object instanceof UnqueueMessage) || (object instanceof Logout)) {
            JwtAuthenticationUser user = jwtModule.getUser(connection.getID());
            // Successful login
            if (user != null) {
                LOG.info(user.login + " unqueued.");
                playersInQueue.remove(user.id);
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
        long playerId = playersInQueue.keySet().iterator().next();
        return playersInQueue.remove(playerId);
    }
}
