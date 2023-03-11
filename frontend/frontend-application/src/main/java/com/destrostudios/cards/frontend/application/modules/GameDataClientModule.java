package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.network.messages.*;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryonet.Connection;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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
    private PackResult packResult;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof InitialGameDataMessage initialGameDataMessage) {
            modes = initialGameDataMessage.getModes();
            cards = initialGameDataMessage.getCards();
            user = initialGameDataMessage.getUser();
            userCardLists = initialGameDataMessage.getUserCardLists();
        } else if (object instanceof UserCardListsMessage userCardListsMessage) {
            userCardLists = userCardListsMessage.getUserCardLists();
        } else if (object instanceof PackResultMessage packResultMessage) {
            user = packResultMessage.getUser();
            userCardLists = packResultMessage.getUserCardLists();
            packResult = packResultMessage.getPackResult();
        }
    }

    public UserCardList getLibrary(int modeId) {
        return userCardLists.stream().filter(ucl -> ucl.getMode().getId() == modeId && ucl.isLibrary()).findFirst().orElseThrow();
    }

    public List<UserCardList> getDecks(int modeId) {
        return userCardLists.stream().filter(ucl -> ucl.getMode().getId() == modeId && !ucl.isLibrary()).collect(Collectors.toList());
    }

    public void createUserCardList(int modeId) {
        userCardLists = null;
        connection.sendTCP(new CreateUserCardListMessage(modeId));
    }

    public void updateUserCardList(int userCardListId, String name, List<NewCardListCard> cards) {
        userCardLists = null;
        connection.sendTCP(new UpdateUserCardListMessage(userCardListId, name, cards));
    }

    public void deleteUserCardList(int userCardListId) {
        userCardLists = null;
        connection.sendTCP(new DeleteUserCardListMessage(userCardListId));
    }

    public void openPack() {
        user = null;
        userCardLists = null;
        packResult = null;
        connection.sendTCP(new OpenPackMessage());
    }
}