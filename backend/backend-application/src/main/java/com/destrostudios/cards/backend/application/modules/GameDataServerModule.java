package com.destrostudios.cards.backend.application.modules;

import amara.libraries.database.Database;
import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.services.CardService;
import com.destrostudios.cards.backend.application.services.ModeService;
import com.destrostudios.cards.backend.application.services.PackService;
import com.destrostudios.cards.backend.application.services.UserService;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.model.UserCardList;
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
    private ModeService modeService;
    private CardService cardService;
    private UserService userService;
    private PackService packService;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Login) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            database.transaction(() -> {
                List<Mode> modes = modeService.getModes();
                List<Card> cards = cardService.getCards();
                User user = userService.getOrCreateUser(jwtUser);
                List<UserCardList> userCardLists = userService.getUserCardLists(user.getId());
                connection.sendTCP(new InitialGameDataMessage(modes, cards, user, userCardLists));
            });
        } else if (object instanceof CreateUserCardListMessage createUserCardListMessage) {
            database.transaction(() -> userService.createUserCardList(getUserId(connection), createUserCardListMessage.getModeId(), false));
            sendUserCardLists(connection);
        } else if (object instanceof UpdateUserCardListMessage updateUserCardListMessage) {
            database.transaction(() -> userService.updateUserCardList(updateUserCardListMessage.getUserCardListId(), updateUserCardListMessage.getName(), updateUserCardListMessage.getCards()));
            sendUserCardLists(connection);
        } else if (object instanceof DeleteUserCardListMessage deleteUserCardListMessage) {
            database.transaction(() -> userService.deleteUserCardList(deleteUserCardListMessage.getUserCardListId()));
            sendUserCardLists(connection);
        } else if (object instanceof OpenPackMessage) {
            int userId = getUserId(connection);
            database.transaction(() -> {
                PackResult packResult = packService.openPack(userId);
                List<UserCardList> userCardLists = userService.getUserCardLists(userId);
                connection.sendTCP(new PackResultMessage(packResult, userCardLists));
            });
        }
    }

    private void sendUserCardLists(Connection connection) {
        int userId = getUserId(connection);
        List<UserCardList> userCardLists = userService.getUserCardLists(userId);
        connection.sendTCP(new UserCardListsMessage(userCardLists));
    }

    private int getUserId(Connection connection) {
        JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
        return (int) jwtUser.id;
    }
}
