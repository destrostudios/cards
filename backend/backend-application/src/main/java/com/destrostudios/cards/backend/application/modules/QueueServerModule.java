package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotModule;
import com.destrostudios.cards.backend.application.services.DeckService;
import com.destrostudios.cards.backend.application.services.ModeService;
import com.destrostudios.cards.backend.application.services.QueueService;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.Deck;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.network.messages.QueueMessage;
import com.destrostudios.cards.shared.network.messages.UnqueueMessage;
import com.destrostudios.cards.shared.rules.GameConstants;
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

    public QueueServerModule(JwtServerModule jwtModule, CardsGameStartServerModule cardsGameStartServerModule, CardsBotModule cardsBotModule, ModeService modeService, DeckService deckService, QueueService queueService) {
        this.jwtModule = jwtModule;
        this.cardsGameStartServerModule = cardsGameStartServerModule;
        this.cardsBotModule = cardsBotModule;
        this.modeService = modeService;
        this.deckService = deckService;
        this.queueService = queueService;
        modeQueuePlayers = new HashMap<>();
    }
    public static final int BOT_USER_ID = 0;
    public static final String BOT_USER_NAME = "Bot";
    private static final String BOARD_NAME = "forest";
    private JwtServerModule jwtModule;
    private CardsGameStartServerModule cardsGameStartServerModule;
    private CardsBotModule cardsBotModule;
    private ModeService modeService;
    private DeckService deckService;
    private QueueService queueService;
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, PlayerInfo>>> modeQueuePlayers;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof QueueMessage queueMessage) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            // Successful login
            if (jwtUser != null) {
                int userId = (int) jwtUser.id;
                LOG.info(jwtUser.login + " queued up (" + queueMessage + ").");
                Mode mode = modeService.getMode(queueMessage.getModeId());
                Deck deck = deckService.getDeck(queueMessage.getModeId(), queueMessage.getDeckId());
                PlayerInfo playerInfo = new PlayerInfo(userId, jwtUser.login, deck.getDeckCardList());
                Queue queue = queueService.getQueue(queueMessage.getQueueId());
                if (queue.getName().equals(GameConstants.QUEUE_NAME_USER)) {
                    modeQueuePlayers
                            .computeIfAbsent(mode.getId(), mid -> new HashMap<>())
                            .computeIfAbsent(queue.getId(), qid -> new HashMap<>())
                            .put(userId, playerInfo);
                    startGameIfPossible(mode, queue);
                } else {
                    CardList botDeck = null;
                    if (mode.getDecks().size() > 0) {
                        botDeck = mode.getDecks().get((int) (Math.random() * mode.getDecks().size())).getDeckCardList();
                    }
                    UUID gameId = cardsGameStartServerModule.startGame(new StartGameInfo(
                        mode,
                        queue,
                        BOARD_NAME,
                        new PlayerInfo[] {
                            playerInfo,
                            new PlayerInfo(BOT_USER_ID, BOT_USER_NAME, botDeck)
                        }
                    ));
                    cardsBotModule.checkBotTurn(gameId);
                }
            }
        } else if ((object instanceof UnqueueMessage) || (object instanceof Logout)) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            // Successful login
            if (jwtUser != null) {
                LOG.info(jwtUser.login + " unqueued.");
                modeQueuePlayers.values()
                        .forEach(queuePlayers -> queuePlayers.values()
                        .forEach(players -> players.remove((int) jwtUser.id)));
            }
        }
    }

    private void startGameIfPossible(Mode mode, Queue queue) {
        HashMap<Integer, PlayerInfo> players = modeQueuePlayers.get(mode.getId()).get(queue.getId());
        if (players.size() > 1) {
            PlayerInfo playerInfo1 = popPlayerInfo(players);
            PlayerInfo playerInfo2 = popPlayerInfo(players);
            cardsGameStartServerModule.startGame(new StartGameInfo(mode, queue, BOARD_NAME, new PlayerInfo[] { playerInfo1, playerInfo2 }));
        }
    }

    private PlayerInfo popPlayerInfo(HashMap<Integer, PlayerInfo> queuedPlayers) {
        int playerId = queuedPlayers.keySet().iterator().next();
        return queuedPlayers.remove(playerId);
    }
}
