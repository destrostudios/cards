package com.destrostudios.cards.backend.application.modules;

import amara.libraries.database.Database;
import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.services.CardService;
import com.destrostudios.cards.backend.application.services.ModeService;
import com.destrostudios.cards.backend.application.services.QueueService;
import com.destrostudios.cards.backend.application.services.UserService;
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
        } else if (object instanceof CreateUserModeDeckMessage createUserModeDeckMessage) {
            database.transaction(() -> userService.createUserModeDeck(createUserModeDeckMessage.getUserModeId()));
            sendUser(connection);
        } else if (object instanceof UpdateUserModeDeckMessage updateUserModeDeckMessage) {
            database.transaction(() -> userService.updateUserModeDeck(updateUserModeDeckMessage.getUserModeDeckId(), updateUserModeDeckMessage.getName(), updateUserModeDeckMessage.getCards()));
            sendUser(connection);
        } else if (object instanceof DeleteUserModeDeckMessage deleteUserModeDeckMessage) {
            database.transaction(() -> userService.deleteUserModeDeck(deleteUserModeDeckMessage.getUserModeDeckId()));
            sendUser(connection);
        } else if (object instanceof GetUserMessage) {
            sendUser(connection);
        } else if (object instanceof OpenPackMessage openPackMessage) {
            int userId = getUserId(connection);
            database.transaction(() -> {
                PackResult packResult = userService.openPack(openPackMessage.getUserModeId());
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
