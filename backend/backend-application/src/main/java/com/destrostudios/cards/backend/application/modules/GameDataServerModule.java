package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.services.*;
import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.network.messages.*;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.destrostudios.gametools.network.shared.modules.jwt.messages.Login;
import com.esotericsoftware.kryonet.Connection;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GameDataServerModule extends NetworkModule {

    private JwtServerModule jwtModule;
    private Database database;
    private CardService cardService;
    private ModeService modeService;
    private QueueService queueService;
    private UserService userService;
    private DeckService deckService;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Login) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            database.transaction(() -> {
                List<Card> cards = cardService.getCards();
                List<Mode> modes = modeService.getModes();
                List<Queue> queues = queueService.getQueues();
                userService.onLogin(jwtUser);
                User user = userService.getUser(getUserId(connection));
                connection.sendTCP(new InitialGameDataMessage(cards, modes, queues, user));
            });
        } else if (object instanceof CreateDeckMessage createDeckMessage) {
            database.transaction(() -> deckService.createDeck(getUserId(connection), createDeckMessage.getModeId()));
            sendUser(connection);
        } else if (object instanceof UpdateDeckMessage updateDeckMessage) {
            database.transaction(() -> deckService.updateDeck(updateDeckMessage.getModeId(), updateDeckMessage.getDeckId(), updateDeckMessage.getName(), updateDeckMessage.getCards()));
            sendUser(connection);
        } else if (object instanceof DeleteDeckMessage deleteDeckMessage) {
            database.transaction(() -> deckService.deleteDeck(deleteDeckMessage.getModeId(), deleteDeckMessage.getDeckId()));
            sendUser(connection);
        } else if (object instanceof GetUserMessage) {
            sendUser(connection);
        } else if (object instanceof OpenPackMessage) {
            int userId = getUserId(connection);
            database.transaction(() -> {
                PackResult packResult = userService.openPack(userId);
                User user = userService.getUser(userId);
                connection.sendTCP(new PackResultMessage(packResult, user));
            });
        }
    }

    private void sendUser(Connection connection) {
        User user = userService.getUser(getUserId(connection));
        connection.sendTCP(new UserMessage(user));
    }

    private int getUserId(Connection connection) {
        JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
        return (int) jwtUser.id;
    }
}
