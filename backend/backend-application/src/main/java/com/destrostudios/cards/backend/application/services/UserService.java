package com.destrostudios.cards.backend.application.services;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.backend.application.services.internal.NewDeck;
import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.backend.database.databases.QueryResult;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.BaseCardIdentifier;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.rules.GameConstants;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserService {

    private Database database;
    private ModeService modeService;
    private CardService cardService;
    private FoilService foilService;
    private CardListService cardListService;
    private QueueService queueService;
    private ArenaService arenaService;

    public void onLogin(JwtAuthenticationUser jwtUser) {
        String escapedNow = database.getEscapedNow();
        int userId = (int) jwtUser.id;
        if (hasUser(userId)) {
            database.execute("UPDATE user SET last_login_date = '" + escapedNow + "' WHERE id = " + userId);
        } else {
            int collectionCardListId = cardListService.createCardList();
            database.execute("INSERT INTO user (id, login, admin, collection_card_list_id, packs, packs_opened, arena_seed, first_login_date, last_login_date) VALUES (" + userId + ", '" + database.escape(jwtUser.login) + "', FALSE, " + collectionCardListId + ", " + GameConstants.PACKS_FOR_NEW_PLAYERS + ", 0, " + arenaService.generateSeed() + ", '" + escapedNow + "', '" + escapedNow + "')");
            createIntroductionDeck(userId);
        }
    }

    private boolean hasUser(int userId) {
        try (QueryResult result = database.select("SELECT id FROM user WHERE id = " + userId)) {
            return result.next();
        }
    }

    private void createIntroductionDeck(int userId) {
        Mode classicMode = modeService.getMode(GameConstants.MODE_NAME_CLASSIC);
        NewDeck introductionDeck = createUserModeDeck(userId, classicMode.getId());
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);
        List<NewCardListCard> cards = cardService.getCards_Core().stream()
                .map(card -> new NewCardListCard(card.getId(), foilNone.getId(), 2))
                .collect(Collectors.toList());
        updateUserModeDeck(introductionDeck.getId(), "Introduction Deck", cards);
    }

    public User getUser(int userId) {
        try (QueryResult result = database.select("SELECT * FROM user WHERE id = " + userId)) {
            result.next();
            long arenaSeed = result.getLong("arena_seed");
            return new User(
                result.getInteger("id"),
                result.getString("login"),
                result.getBoolean("admin"),
                getCollectionCardList(userId, result.getInteger("collection_card_list_id")),
                result.getInteger("packs"),
                result.getInteger("packs_opened"),
                arenaSeed,
                getArenaDraftCards(arenaSeed),
                result.getDateTime("first_login_date"),
                result.getDateTime("last_login_date"),
                getUserModeDecks(userId),
                getUserModeQueues(userId)
            );
        }
    }

    public CardList getCollectionCardList(int userId) {
        try (QueryResult result = database.select("SELECT collection_card_list_id FROM user WHERE id = " + userId)) {
            result.next();
            int collectionCardListId = result.getInteger("collection_card_list_id");
            return getCollectionCardList(userId, collectionCardListId);
        }
    }

    private CardList getCollectionCardList(int userId, int collectionCardListId) {
        CardList cardList = cardListService.getCardList(collectionCardListId);
        if (isAdmin(userId)) {
            addAllCardsToList(cardList);
        } else {
            addCoreCardsToList(cardList);
        }
        return cardList;
    }

    private boolean isAdmin(int userId) {
        try (QueryResult result = database.select("SELECT admin FROM user WHERE id = " + userId)) {
            result.next();
            return result.getBoolean("admin");
        }
    }

    private void addAllCardsToList(CardList cardList) {
        for (Card card : cardService.getCards()) {
            for (Foil foil : foilService.getFoils()) {
                cardList.getCards().add(new CardListCard(0, card, foil, 2));
            }
        }
    }

    private void addCoreCardsToList(CardList cardList) {
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);
        for (Card card : cardService.getCards_Core()) {
            cardList.getCards().add(new CardListCard(0, card, foilNone, 2));
        }
    }

    private List<UserModeDeck> getUserModeDecks(int userId) {
        LinkedList<UserModeDeck> userModeDecks = new LinkedList<>();
        try (QueryResult result = database.select("SELECT * FROM user_mode_deck WHERE user_id = " + userId)) {
            while (result.next()) {
                userModeDecks.add(mapUserModeDeck(result));
            }
        }
        return userModeDecks;
    }

    public UserModeDeck getUserModeDeck(int userModeDeckId) {
        try (QueryResult result = database.select("SELECT * FROM user_mode_deck WHERE id = " + userModeDeckId)) {
            result.next();
            return mapUserModeDeck(result);
        }
    }

    private UserModeDeck getArenaDeck(int userId) {
        Mode arenaMode = modeService.getMode(GameConstants.MODE_NAME_ARENA);
        try (QueryResult result = database.select("SELECT * FROM user_mode_deck WHERE user_id = " + userId + " AND mode_id = " + arenaMode.getId())) {
            if (result.next()) {
                return mapUserModeDeck(result);
            }
            return null;
        }
    }

    private UserModeDeck mapUserModeDeck(QueryResult result) {
        return new UserModeDeck(
            result.getInteger("id"),
            result.getInteger("user_id"),
            modeService.getMode(result.getInteger("mode_id")),
            cardListService.getCardList(result.getInteger("deck_card_list_id"))
        );
    }

    public NewDeck createUserModeDeck(int userId, int modeId) {
        int deckCardListId = cardListService.createCardList();
        try (QueryResult result = database.insert("INSERT INTO user_mode_deck (user_id, mode_id, deck_card_list_id) VALUES (" + userId + ", " + modeId + ", " + deckCardListId + ")")) {
            result.next();
            int id = result.getInteger(1);
            return new NewDeck(id, deckCardListId);
        }
    }

    public void updateUserModeDeck(int userModeDeckId, String name, List<NewCardListCard> cards) {
        UserModeDeck userModeDeck = getUserModeDeck(userModeDeckId);
        cardListService.updateCardList(userModeDeck.getDeckCardList().getId(), name, cards);
    }

    public void deleteUserModeDeck(int userModeDeckId) {
        UserModeDeck userModeDeck = getUserModeDeck(userModeDeckId);
        database.execute("DELETE FROM user_mode_deck WHERE id = " + userModeDeck.getId());
        cardListService.deleteCardList(userModeDeck.getDeckCardList().getId());
    }

    private List<UserModeQueue> getUserModeQueues(int userId) {
        LinkedList<UserModeQueue> userModeQueues = new LinkedList<>();
        try (QueryResult result = database.select("SELECT * FROM user_mode_queue WHERE user_id = " + userId)) {
            while (result.next()) {
                userModeQueues.add(mapUserModeQueue(result));
            }
        }
        return userModeQueues;
    }

    private UserModeQueue getUserModeQueue(int userId, int modeId, int queueId) {
        try (QueryResult result = database.select("SELECT * FROM user_mode_queue WHERE user_id = " + userId + " AND mode_id = " + modeId + " AND queue_id = " + queueId)) {
            if (result.next()) {
                return mapUserModeQueue(result);
            }
        }
        return null;
    }

    private UserModeQueue mapUserModeQueue(QueryResult result) {
        return new UserModeQueue(
            result.getInteger("id"),
            result.getInteger("user_id"),
            modeService.getMode(result.getInteger("mode_id")),
            queueService.getQueue(result.getInteger("queue_id")),
            result.getInteger("games"),
            result.getInteger("wins"),
            result.getInteger("current_win_streak"),
            result.getInteger("longest_win_streak"),
            result.getDateTime("first_game_date"),
            result.getDateTime("last_game_date")
        );
    }

    public void onGameOver(int userId, int modeId, int queueId, boolean win) {
        String escapedNow = database.getEscapedNow();
        UserModeQueue userModeQueue = getUserModeQueue(userId, modeId, queueId);
        if (userModeQueue == null) {
            int wins = (win ? 1 : 0);
            database.execute("INSERT INTO user_mode_queue (user_id, mode_id, queue_id, games, wins, current_win_streak, longest_win_streak, first_game_date, last_game_date) VALUES (" + userId + ", " + modeId + ", " + queueId + ", 1, " + wins + ", " + wins + ", " + wins + ", '" + escapedNow + "', '" + escapedNow + "')");
        } else {
            int currentWinStreak = 0;
            Integer newLongestWinStreak = null;
            if (win) {
                currentWinStreak = userModeQueue.getCurrentWinStreak() + 1;
                if (currentWinStreak > userModeQueue.getLongestWinStreak()) {
                    newLongestWinStreak = currentWinStreak;
                }
            }
            database.execute("UPDATE user_mode_queue SET games = games + 1" + (win ? ", wins = wins + 1" : "") + ", current_win_streak = " + currentWinStreak + ((newLongestWinStreak != null) ? ", longest_win_streak = " + newLongestWinStreak : "") + ", last_game_date = '" + escapedNow + "' WHERE id = " + userModeQueue.getId());
        }
    }

    public void addPacks(int userId, int packs) {
        database.execute("UPDATE user SET packs = packs + " + packs + " WHERE id = " + userId);
    }

    public PackResult openPack(int userId) {
        User user = getUser(userId);
        if (user.getPacks() <= 0) {
            throw new RuntimeException("User has no packs.");
        }
        database.execute("UPDATE user SET packs = packs - 1, packs_opened = packs_opened + 1 WHERE id = " + userId);
        PackResult packResult = createPackResult();
        CardList collectionCardList = getCollectionCardList(userId);
        for (CardIdentifier cardIdentifier : packResult.getCards()) {
            cardListService.addCard(collectionCardList.getId(), cardIdentifier.getCard().getId(), cardIdentifier.getFoil().getId(), 1);
        }
        return packResult;
    }

    private PackResult createPackResult() {
        List<Card> availableCards = cardService.getCards_NonCore();
        Foil foilArtwork = foilService.getFoil(GameConstants.FOIL_NAME_ARTWORK);
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);
        LinkedList<BaseCardIdentifier> cards = new LinkedList<>();
        for (int i = 0; i < GameConstants.CARDS_PER_PACK; i++) {
            int cardIndex = (int) (Math.random() * availableCards.size());
            Card card = availableCards.get(cardIndex);
            Foil foil = ((i == 0) ? foilArtwork : foilNone);
            cards.add(new BaseCardIdentifier(card, foil));
        }
        return new PackResult(cards);
    }

    public void addArenaCard(int userId, int cardId) {
        User user = getUser(userId);
        List<BaseCardIdentifier> cardIdentifiers = getArenaDraftCards(user.getArenaSeed());
        BaseCardIdentifier cardIdentifier = cardIdentifiers.stream().filter(draftCard -> draftCard.getCard().getId() == cardId).findAny().orElse(null);
        if (cardIdentifier == null) {
            throw new RuntimeException("Selected card is not a possible choice.");
        }
        int deckCardListId = getOrCreateArenaDeckCardListId(userId);
        cardListService.addCard(deckCardListId, cardIdentifier.getCard().getId(), cardIdentifier.getFoil().getId(), 1);
        database.execute("UPDATE user SET arena_seed = " + arenaService.generateSeed() + " WHERE id = " + userId);
    }

    private List<BaseCardIdentifier> getArenaDraftCards(long arenaSeed) {
        List<Card> cards = arenaService.getDraftCards(arenaSeed);
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);
        return cards.stream().map(card -> new BaseCardIdentifier(card, foilNone)).collect(Collectors.toList());
    }

    private int getOrCreateArenaDeckCardListId(int userId) {
        UserModeDeck arenaDeck = getArenaDeck(userId);
        if (arenaDeck != null) {
            return arenaDeck.getDeckCardList().getId();
        } else {
            Mode arenaMode = modeService.getMode(GameConstants.MODE_NAME_ARENA);
            NewDeck newArenaDeck = createUserModeDeck(userId, arenaMode.getId());
            return newArenaDeck.getDeckCardListId();
        }
    }
}
