package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.shared.model.*;
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

    public void onLogin(JwtAuthenticationUser jwtUser) {
        int userId = (int) jwtUser.id;
        String escapedNow = database.getEscapedNow();
        if (hasUser(userId)) {
            database.execute("UPDATE user SET last_login_date = '" + escapedNow + "' WHERE id = " + userId);
        } else {
            database.execute("INSERT INTO user (id, login, admin, first_login_date, last_login_date) VALUES (" + userId + ", '" + database.escape(jwtUser.login) + "', FALSE, '" + escapedNow + "', '" + escapedNow + "')");
        }
        // Always do this, in case of new modes being introduced to existing users
        createUserModesIfNotExisting(userId);
    }

    private boolean hasUser(int userId) {
        try (QueryResult result = database.select("SELECT id FROM user WHERE id = " + userId)) {
            return result.next();
        }
    }

    private void createUserModesIfNotExisting(int userId) {
        for (Mode mode : modeService.getModes()) {
            if (!hasUserMode(userId, mode.getId())) {
                createUserMode(userId, mode);
            }
        }
    }

    private boolean hasUserMode(int userId, int modeId) {
        try (QueryResult result = database.select("SELECT id FROM user_mode WHERE user_id = " + userId + " AND mode_id = " + modeId)) {
            return result.next();
        }
    }

    private void createUserMode(int userId, Mode mode) {
        Integer collectionCardListId = null;
        if (mode.isHasUserLibrary()) {
            collectionCardListId = cardListService.createCardList();
        }
        boolean isClassicMode = mode.getName().equals(GameConstants.MODE_NAME_CLASSIC);
        int packs = (isClassicMode ? GameConstants.PACKS_FOR_NEW_PLAYERS_CLASSIC : 0);
        try (QueryResult result = database.insert("INSERT INTO user_mode (user_id, mode_id, collection_card_list_id, packs, packs_opened) VALUES (" + userId + ", " + mode.getId() + ", " + collectionCardListId + ", " + packs + ", 0)")) {
            result.next();
            int userModeId = result.getInteger(1);
            if (isClassicMode) {
                createIntroductionDeck(userModeId);
            }
        }
    }

    private void createIntroductionDeck(int userModeId) {
        int introductionDeckId = createUserModeDeck(userModeId);
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);
        List<NewCardListCard> cards = cardService.getCards_Core().stream()
                .map(card -> new NewCardListCard(card.getId(), foilNone.getId(), 2))
                .collect(Collectors.toList());
        updateUserModeDeck(introductionDeckId, "Introduction Deck", cards);
    }

    public User getUser(int userId) {
        try (QueryResult result = database.select("SELECT * FROM user WHERE id = " + userId)) {
            result.next();
            return new User(
                result.getInteger("id"),
                result.getString("login"),
                result.getBoolean("admin"),
                result.getDateTime("first_login_date"),
                result.getDateTime("last_login_date"),
                getUserModes(userId)
            );
        }
    }

    private List<UserMode> getUserModes(int userId) {
        LinkedList<UserMode> userModes = new LinkedList<>();
        try (QueryResult result = database.select("SELECT * FROM user_mode WHERE user_id = " + userId)) {
            while (result.next()) {
                userModes.add(mapUserMode(result));
            }
        }
        return userModes;
    }

    private UserMode getUserMode(int userModeId) {
        try (QueryResult result = database.select("SELECT * FROM user_mode WHERE id = " + userModeId)) {
            result.next();
            return mapUserMode(result);
        }
    }

    private UserMode mapUserMode(QueryResult result) {
        int userModeId = result.getInteger("id");
        int userId = result.getInteger("user_id");
        int modeId = result.getInteger("mode_id");
        return new UserMode(
            userModeId,
            userId,
            modeService.getMode(modeId),
            getCollectionCardList(userId, modeId),
            result.getInteger("packs"),
            result.getInteger("packs_opened"),
            getUserModeDecks(userModeId)
        );
    }

    public CardList getCollectionCardList(int userId, int modeId) {
        try (QueryResult result = database.select("SELECT collection_card_list_id FROM user_mode WHERE user_id = " + userId + " AND mode_id = " + modeId)) {
            result.next();
            int cardListId = result.getInteger("collection_card_list_id");
            CardList cardList = cardListService.getCardList(cardListId);
            if (isAdmin(userId)) {
                addAllCardsToList(cardList);
            } else {
                Mode mode = modeService.getMode(modeId);
                if (mode.getName().equals(GameConstants.MODE_NAME_CLASSIC)) {
                    addCoreCardsToList(cardList);
                }
            }
            return cardList;
        }
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

    private List<UserModeDeck> getUserModeDecks(int userModeId) {
        LinkedList<UserModeDeck> userModeDecks = new LinkedList<>();
        try (QueryResult result = database.select("SELECT * FROM user_mode_deck WHERE user_mode_id = " + userModeId)) {
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

    private UserModeDeck mapUserModeDeck(QueryResult result) {
        return new UserModeDeck(
            result.getInteger("id"),
            result.getInteger("user_mode_id"),
            cardListService.getCardList(result.getInteger("deck_card_list_id"))
        );
    }

    public int createUserModeDeck(int userModeId) {
        int deckCardListId = cardListService.createCardList();
        try (QueryResult result = database.insert("INSERT INTO user_mode_deck (user_mode_id, deck_card_list_id) VALUES (" + userModeId + ", " + deckCardListId + ")")) {
            result.next();
            return result.getInteger(1);
        }
    }

    public void updateUserModeDeck(int userModeDeckId, String name, List<NewCardListCard> cards) {
        UserModeDeck userModeDeck = getUserModeDeck(userModeDeckId);
        cardListService.updateCardList(userModeDeck.getDeckCardList().getId(), name, cards);
    }

    public void deleteUserModeDeck(int userModeDeckId) {
        UserModeDeck userModeDeck = getUserModeDeck(userModeDeckId);
        database.execute("DELETE FROM user_mode_deck WHERE id = " + userModeDeckId);
        cardListService.deleteCardList(userModeDeck.getDeckCardList().getId());
    }

    public void addPacks(int userId, int modeId, int additionalPacks) {
        database.execute("UPDATE user_mode SET packs = packs + " + additionalPacks + " WHERE user_id = " + userId + " AND mode_id = " + modeId);
    }

    public PackResult openPack(int userModeId) {
        UserMode userMode = getUserMode(userModeId);
        if (userMode.getPacks() <= 0) {
            throw new RuntimeException("User has no packs for this mode.");
        }
        database.execute("UPDATE user_mode SET packs = packs - 1, packs_opened = packs_opened + 1 WHERE user_id = " + userMode.getUserId() + " AND mode_id = " + userMode.getMode().getId());
        PackResult packResult = createPackResult();
        CardList collectionCardList = getCollectionCardList(userMode.getUserId(), userMode.getMode().getId());
        for (CardListCard cardListCard : packResult.getCards()) {
            cardListService.addCard(collectionCardList.getId(), cardListCard.getCard().getId(), cardListCard.getFoil().getId(), cardListCard.getAmount());
        }
        return packResult;
    }

    private PackResult createPackResult() {
        List<Card> availableCards = cardService.getCards_Pack();
        Foil foilArtwork = foilService.getFoil(GameConstants.FOIL_NAME_ARTWORK);
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);
        LinkedList<CardListCard> cards = new LinkedList<>();
        for (int i = 0; i < GameConstants.CARDS_PER_PACK; i++) {
            int cardIndex = (int) (Math.random() * availableCards.size());
            Card card = availableCards.get(cardIndex);
            Foil foil = ((i == 0) ? foilArtwork : foilNone);
            cards.add(new CardListCard(0, card, foil, 1));
        }
        return new PackResult(cards);
    }
}
