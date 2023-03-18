package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.network.messages.*;
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
    private List<Card> cards;
    private List<Mode> modes;
    private List<Queue> queues;
    private User user;
    private PackResult packResult;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof InitialGameDataMessage initialGameDataMessage) {
            cards = initialGameDataMessage.getCards();
            modes = initialGameDataMessage.getModes();
            queues = initialGameDataMessage.getQueues();
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

    public int getTotalPacks() {
        return user.getModes().stream().map(UserMode::getPacks).reduce(0, Integer::sum);
    }

    public int getPacks(int modeId) {
        return getUserMode(modeId).getPacks();
    }

    public void openPack(int modeId) {
        int userModeId = getUserMode(modeId).getId();
        user = null;
        packResult = null;
        connection.sendTCP(new OpenPackMessage(userModeId));
    }

    private UserMode getUserMode(int modeId) {
        return user.getModes().stream().filter(um -> um.getMode().getId() == modeId).findFirst().orElseThrow();
    }
}
