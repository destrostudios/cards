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

    public CardList getCollection(Mode mode) {
        return getUserMode(mode).getCollectionCardList();
    }

    public List<? extends Deck> getDecks(Mode mode) {
        if (mode.isHasUserLibrary()) {
            return getUserMode(mode).getDecks();
        } else {
            return mode.getDecks();
        }
    }

    public void createDeck(Mode mode) {
        user = null;
        connection.sendTCP(new CreateDeckMessage(mode.getId()));
    }

    public void updateDeck(Mode mode, Deck deck, String name, List<NewCardListCard> cards) {
        user = null;
        connection.sendTCP(new UpdateDeckMessage(mode.getId(), deck.getId(), name, cards));
    }

    public void deleteDeck(Mode mode, Deck deck) {
        user = null;
        connection.sendTCP(new DeleteDeckMessage(mode.getId(), deck.getId()));
    }

    public void refreshUser() {
        user = null;
        connection.sendTCP(new GetUserMessage());
    }

    public int getTotalPacks() {
        return user.getModes().stream().map(UserMode::getPacks).reduce(0, Integer::sum);
    }

    public int getPacks(Mode mode) {
        return getUserMode(mode).getPacks();
    }

    public void openPack(Mode mode) {
        int userModeId = getUserMode(mode).getId();
        user = null;
        packResult = null;
        connection.sendTCP(new OpenPackMessage(userModeId));
    }

    private UserMode getUserMode(Mode mode) {
        return user.getModes().stream().filter(um -> um.getMode().getId() == mode.getId()).findFirst().orElseThrow();
    }
}
