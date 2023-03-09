package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotModule;
import com.destrostudios.cards.backend.application.services.CardListService;
import com.destrostudios.cards.shared.model.CardList;
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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class QueueServerModule extends NetworkModule {

    private static final Logger LOG = LoggerFactory.getLogger(QueueServerModule.class);

    public QueueServerModule(JwtServerModule jwtModule, CardsGameStartServerModule cardsGameStartServerModule, CardsBotModule cardsBotModule, CardListService cardListService) {
        this.jwtModule = jwtModule;
        this.cardsGameStartServerModule = cardsGameStartServerModule;
        this.cardsBotModule = cardsBotModule;
        this.cardListService = cardListService;
        playersInQueue = new HashMap<>();
    }
    private JwtServerModule jwtModule;
    private CardsGameStartServerModule cardsGameStartServerModule;
    private CardsBotModule cardsBotModule;
    private CardListService cardListService;
    private HashMap<Long, PlayerInfo> playersInQueue;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof QueueMessage queueMessage) {
            JwtAuthenticationUser user = jwtModule.getUser(connection.getID());
            // Successful login
            if (user != null) {
                LOG.info(user.login + " queued up (againstHumanOrBot = " + queueMessage.isAgainstHumanOrBot() + ", cardListId = " + queueMessage.getCardListId() + ").");
                CardList cardList = cardListService.getCardList(queueMessage.getCardListId());
                // TODO: Map amounts etc.
                List<String> libraryTemplates = cardList.getCards().stream().map(c -> c.getCard().getPath()).collect(Collectors.toList());
                PlayerInfo playerInfo = new PlayerInfo(user.id, user.login, libraryTemplates);
                if (queueMessage.isAgainstHumanOrBot()) {
                    playersInQueue.put(user.id, playerInfo);
                    startGameIfPossible();
                } else {
                    UUID gameId = cardsGameStartServerModule.startGame(new StartGameInfo("forest", playerInfo, new PlayerInfo(2, "Bot", new LinkedList<>())));
                    cardsBotModule.checkBotTurn(gameId);
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
