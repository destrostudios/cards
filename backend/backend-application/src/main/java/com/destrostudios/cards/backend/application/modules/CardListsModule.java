package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.services.UserService;
import com.destrostudios.cards.shared.model.UserCardList;
import com.destrostudios.cards.shared.network.messages.CreateCardListMessage;
import com.destrostudios.cards.shared.network.messages.DeleteCardListMessage;
import com.destrostudios.cards.shared.network.messages.UpdateCardListMessage;
import com.destrostudios.cards.shared.network.messages.UserCardListsMessage;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryonet.Connection;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CardListsModule extends NetworkModule {

    private JwtServerModule jwtModule;
    private UserService userService;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof CreateCardListMessage createCardListMessage) {
            int userId = getUserId(connection);
            userService.createUserCardList(userId, createCardListMessage.getModeId(), false);
            sendUserCardLists(connection);
        } else if (object instanceof UpdateCardListMessage updateCardListMessage) {
            userService.updateUserCardList(updateCardListMessage.getUserCardListId(), updateCardListMessage.getName(), updateCardListMessage.getCards());
            sendUserCardLists(connection);
        } else if (object instanceof DeleteCardListMessage deleteCardListMessage) {
            userService.deleteUserCardList(deleteCardListMessage.getUserCardListId());
            sendUserCardLists(connection);
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
