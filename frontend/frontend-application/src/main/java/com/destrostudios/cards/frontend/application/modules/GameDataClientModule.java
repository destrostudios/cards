package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.network.messages.*;
import com.destrostudios.cards.shared.rules.GameConstants;
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
    private PackResult packResult;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof InitialGameDataMessage initialGameDataMessage) {
            modes = initialGameDataMessage.getModes();
            cards = initialGameDataMessage.getCards();
            user = initialGameDataMessage.getUser();
        } else if (object instanceof UserMessage userMessage) {
            user = userMessage.getUser();
        } else if (object instanceof PackResultMessage packResultMessage) {
            user = packResultMessage.getUser();
            packResult = packResultMessage.getPackResult();
        }
    }

    public CardList getCollection(int modeId) {
        return getUserMode(modeId).getCollectionCardList();
    }

    public List<UserModeDeck> getDecks(int modeId) {
        return getUserMode(modeId).getDecks();
    }

    public void createUserModeDeck(int modeId) {
        int userModeId = getUserMode(modeId).getId();
        user = null;
        connection.sendTCP(new CreateUserModeDeckMessage(userModeId));
    }

    public void updateUserModeDeck(int userModeDeckId, String name, List<NewCardListCard> cards) {
        user = null;
        connection.sendTCP(new UpdateUserModeDeckMessage(userModeDeckId, name, cards));
    }

    public void deleteUserModeDeck(int userModeDeckId) {
        user = null;
        connection.sendTCP(new DeleteUserModeDeckMessage(userModeDeckId));
    }

    public void refreshUser() {
        user = null;
        connection.sendTCP(new GetUserMessage());
    }

    public int getPacks() {
        return getClassicUserMode().getPacks();
    }

    public void openPack() {
        int userModeId = getClassicUserMode().getId();
        user = null;
        packResult = null;
        connection.sendTCP(new OpenPackMessage(userModeId));
    }

    private UserMode getClassicUserMode() {
        return user.getModes().stream().filter(um -> um.getMode().getName().equals(GameConstants.MODE_NAME_CLASSIC)).findFirst().orElseThrow();
    }

    private UserMode getUserMode(int modeId) {
        return user.getModes().stream().filter(um -> um.getMode().getId() == modeId).findFirst().orElseThrow();
    }
}
