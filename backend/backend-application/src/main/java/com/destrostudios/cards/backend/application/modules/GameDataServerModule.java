package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.services.CardService;
import com.destrostudios.cards.backend.application.services.ModeService;
import com.destrostudios.cards.backend.application.services.UserService;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.model.UserCardList;
import com.destrostudios.cards.shared.network.messages.OwnUserCardListsMessage;
import com.destrostudios.cards.shared.network.messages.GameDataMessage;
import com.destrostudios.gametools.network.server.modules.jwt.JwtServerModule;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.destrostudios.gametools.network.shared.modules.jwt.messages.Login;
import com.esotericsoftware.kryonet.Connection;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GameDataServerModule extends NetworkModule {

    private JwtServerModule jwtModule;
    private ModeService modeService;
    private CardService cardService;
    private UserService userService;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Login) {
            JwtAuthenticationUser jwtUser = jwtModule.getUser(connection.getID());
            List<Mode> modes = modeService.getModes();
            List<Card> cards = cardService.getCards();
            User ownUser = userService.getUser(jwtUser);
            connection.sendTCP(new GameDataMessage(modes, cards, ownUser));

            List<UserCardList> userCardLists = userService.getUserCardLists(ownUser.getId());
            connection.sendTCP(new OwnUserCardListsMessage(userCardLists));
        }
    }
}
