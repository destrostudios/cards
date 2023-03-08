package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.network.messages.CardPackResultMessage;
import com.destrostudios.cards.shared.network.messages.InitialGameDataMessage;
import com.destrostudios.cards.shared.network.messages.OpenCardPackMessage;
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
    private User user;
    private List<UserCardList> userCardLists;
    private List<CardListCard> cardPackCards;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof InitialGameDataMessage initialGameDataMessage) {
            modes = initialGameDataMessage.getModes();
            cards = initialGameDataMessage.getCards();
            user = initialGameDataMessage.getUser();
            userCardLists = initialGameDataMessage.getUserCardLists();
        } else if (object instanceof CardPackResultMessage cardPackResultMessage) {
            userCardLists = cardPackResultMessage.getUserCardLists();
            cardPackCards = cardPackResultMessage.getCardPackCards();
        }
    }

    public void openCardPack() {
        userCardLists = null;
        cardPackCards = null;
        connection.sendTCP(new OpenCardPackMessage());
    }
}
