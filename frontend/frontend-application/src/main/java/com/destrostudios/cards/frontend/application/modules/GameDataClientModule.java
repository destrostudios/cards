package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.model.UserCardList;
import com.destrostudios.cards.shared.network.messages.GetOwnUserCardListsMessage;
import com.destrostudios.cards.shared.network.messages.OwnUserCardListsMessage;
import com.destrostudios.cards.shared.network.messages.StaticGameDataMessage;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryonet.Connection;
import lombok.Getter;

import java.util.List;

@Getter
public class GameDataClientModule extends NetworkModule {

    public GameDataClientModule(Connection connection) {
        this.connection = connection;
    }
    private Connection connection;
    private List<Mode> modes;
    private List<Card> cards;
    private User ownUser;
    private List<UserCardList> userCardLists;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof StaticGameDataMessage staticGameDataMessage) {
            modes = staticGameDataMessage.getModes();
            cards = staticGameDataMessage.getCards();
            ownUser = staticGameDataMessage.getOwnUser();
        } else if (object instanceof OwnUserCardListsMessage ownUserCardListsMessage) {
            userCardLists = ownUserCardListsMessage.getUserCardLists();
        }
    }

    public void requestOwnUserCardLists() {
        userCardLists = null;
        connection.sendTCP(new GetOwnUserCardListsMessage());
    }
}
