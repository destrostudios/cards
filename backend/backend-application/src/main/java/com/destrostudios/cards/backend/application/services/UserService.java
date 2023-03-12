package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
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

    public User getOrCreateUser(JwtAuthenticationUser jwtUser) {
        int userId = (int) jwtUser.id;
        User user = getUser(userId);
        if (user == null) {
            user = createUser(userId, jwtUser.login);
        }
        return user;
    }

    public User getUser(int userId) {
        try (QueryResult result = database.select("SELECT * FROM user WHERE id = " + userId)) {
            User user;
            if (result.next()) {
                user = new User(result.getInteger("id"), result.getString("login"), result.getBoolean("admin"), result.getInteger("packs"));
            } else {
                return null;
            }
            return user;
        }
    }

    private User createUser(int userId, String login) {
        User user = new User(userId, login, false, GameConstants.PACKS_FOR_NEW_PLAYERS);
        database.execute("INSERT INTO user (id, login, admin, packs) VALUES (" + user.getId() + ", '" + database.escape(user.getLogin()) + "', FALSE, " + user.getPacks() + ")");
        for (Mode mode : modeService.getModes()) {
            createUserCardList(user.getId(), mode.getId(), true);
        }
        createIntroDeck(user.getId());
        return user;
    }

    private void createIntroDeck(int userId) {
        Mode modeClassic = modeService.getMode(GameConstants.MODE_NAME_CLASSIC);
        int introDeckId = createUserCardList(userId, modeClassic.getId(), false);
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);
        List<NewCardListCard> cards = cardService.getCards_Core().stream()
            .map(card -> new NewCardListCard(card.getId(), foilNone.getId(), 2))
            .collect(Collectors.toList());
        updateUserCardList(introDeckId, "Introduction Deck", cards);
    }

    public int createUserCardList(int userId, int modeId, boolean isLibrary) {
        int cardListId = cardListService.createCardList();
        try (QueryResult result = database.insert("INSERT INTO user_card_list (user_id, mode_id, library, card_list_id) VALUES (" + userId + ", " + modeId + ", " + isLibrary + ", " + cardListId + ")")) {
            result.next();
            return result.getInteger(1);
        }
    }

    public void updateUserCardList(int userCardListId, String name, List<NewCardListCard> cards) {
        UserCardList userCardList = getUserCardList(userCardListId);
        cardListService.updateCardList(userCardList.getCardList().getId(), name, cards);
    }

    public void deleteUserCardList(int userCardListId) {
        UserCardList userCardList = getUserCardList(userCardListId);
        database.execute("DELETE FROM user_card_list WHERE id = " + userCardListId);
        cardListService.deleteCardList(userCardList.getCardList().getId());
    }

    public List<UserCardList> getUserCardLists(int userId) {
        LinkedList<UserCardList> userCardLists = new LinkedList<>();
        try (QueryResult result = database.select("SELECT * FROM user_card_list WHERE user_id = " + userId)) {
            while (result.next()) {
                userCardLists.add(mapUserList(result));
            }
        }
        return userCardLists;
    }

    public UserCardList getUserCardList(int userCardListId) {
        try (QueryResult result = database.select("SELECT * FROM user_card_list WHERE id = " + userCardListId)) {
            result.next();
            return mapUserList(result);
        }
    }

    public UserCardList getLibrary(int userId, int modeId) {
        try (QueryResult result = database.select("SELECT * FROM user_card_list WHERE user_id = " + userId + " AND mode_id = " + modeId + " AND library = TRUE")) {
            result.next();
            return mapUserList(result);
        }
    }

    private UserCardList mapUserList(QueryResult result) {
        int id = result.getInteger("id");
        Mode mode = modeService.getMode(result.getInteger("mode_id"));
        boolean library = result.getBoolean("library");

        CardList cardList = cardListService.getCardList(result.getInteger("card_list_id"));
        if (library) {
            int userId = result.getInteger("user_id");
            User user = getUser(userId);
            if (user.isAdmin()) {
                addAllCardsToList(cardList);
            } else if (mode.getName().equals(GameConstants.MODE_NAME_CLASSIC)) {
                addCoreCardsToList(cardList);
            }
        }

        return new UserCardList(id, mode, library, cardList);
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

    public void addPacks(int userId, int additionalPacks) {
        database.execute("UPDATE user SET packs = packs + " + additionalPacks + " WHERE id = " + userId);
    }
}
