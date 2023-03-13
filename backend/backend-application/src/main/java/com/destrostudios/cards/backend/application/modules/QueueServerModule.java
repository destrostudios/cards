package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotModule;
import com.destrostudios.cards.backend.application.services.ModeService;
import com.destrostudios.cards.backend.application.services.UserService;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.UserModeDeck;
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

    public QueueServerModule(JwtServerModule jwtModule, CardsGameStartServerModule cardsGameStartServerModule, CardsBotModule cardsBotModule, ModeService modeService, UserService userService) {
        this.jwtModule = jwtModule;
        this.cardsGameStartServerModule = cardsGameStartServerModule;
        this.cardsBotModule = cardsBotModule;
        this.modeService = modeService;
        this.userService = userService;
        modeQueuedPlayers = new HashMap<>();
    }
    public static final int USER_ID_BOT = 0;
    private static final String BOARD_NAME = "forest";
    private JwtServerModule jwtModule;
    private CardsGameStartServerModule cardsGameStartServerModule;
    private CardsBotModule cardsBotModule;
    private ModeService modeService;
    private UserService userService;
    private HashMap<Integer, HashMap<Integer, PlayerInfo>> modeQueuedPlayers;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof QueueMessage queueMessage) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            // Successful login
            if (jwtUser != null) {
                int userId = (int) jwtUser.id;
                LOG.info(jwtUser.login + " queued up (" + queueMessage + ").");
                Mode mode = modeService.getMode(queueMessage.getModeId());
                UserModeDeck userModeDeck = userService.getUserModeDeck(queueMessage.getUserModeDeckId());
                PlayerInfo playerInfo = new PlayerInfo(userId, jwtUser.login, userModeDeck);
                if (queueMessage.isAgainstHumanOrBot()) {
                    modeQueuedPlayers.computeIfAbsent(mode.getId(), mid -> new HashMap<>()).put(userId, playerInfo);
                    startGameIfPossible(mode);
                } else {
                    UUID gameId = cardsGameStartServerModule.startGame(new StartGameInfo(
                        mode,
                        BOARD_NAME,
                        playerInfo,
                        new PlayerInfo(USER_ID_BOT, "Bot", null)
                    ));
                    cardsBotModule.checkBotTurn(gameId);
                }
            }
        } else if ((object instanceof UnqueueMessage) || (object instanceof Logout)) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            // Successful login
            if (jwtUser != null) {
                LOG.info(jwtUser.login + " unqueued.");
                modeQueuedPlayers.values().forEach(queuedPlayers -> queuedPlayers.remove((int) jwtUser.id));
            }
        }
    }

    private void startGameIfPossible(Mode mode) {
        HashMap<Integer, PlayerInfo> queuedPlayers = modeQueuedPlayers.get(mode.getId());
        if (queuedPlayers.size() > 1) {
            PlayerInfo playerInfo1 = popPlayerInfo(queuedPlayers);
            PlayerInfo playerInfo2 = popPlayerInfo(queuedPlayers);
            cardsGameStartServerModule.startGame(new StartGameInfo(mode, BOARD_NAME, playerInfo1, playerInfo2));
        }
    }

    private PlayerInfo popPlayerInfo(HashMap<Integer, PlayerInfo> queuedPlayers) {
        int playerId = queuedPlayers.keySet().iterator().next();
        return queuedPlayers.remove(playerId);
    }
}
