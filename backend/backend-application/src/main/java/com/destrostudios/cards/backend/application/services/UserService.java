package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.changes.NewCardListCard;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class UserService {

    private Database database;
    private ModeService modeService;
    private CardService cardService;
    private FoilService foilService;
    private CardListService cardListService;

    public User getUser(JwtAuthenticationUser jwtUser) {
        try (QueryResult result = database.select("SELECT * FROM user WHERE id = " + jwtUser.id)) {
            User user;
            if (result.next()) {
                user = new User(result.getInteger("id"), result.getString("login"));
            } else {
                user = createUser(jwtUser);
            }
            return user;
        }
    }

    private User createUser(JwtAuthenticationUser jwtUser) {
        return database.transaction(() -> {
            User user = new User((int) jwtUser.id, jwtUser.login);
            database.execute("INSERT INTO user (id, login) VALUES (" + user.getId() + ", '" + database.escape(user.getLogin()) + "')");
            for (Mode mode : modeService.getModes()) {
                createUserCardList(user.getId(), mode.getId(), true);
            }
            return user;
        });
    }

    public void createUserCardList(int userId, int modeId, boolean isLibrary) {
        int cardListId = cardListService.createCardList();
        database.execute("INSERT INTO user_card_list (user_id, mode_id, library, card_list_id) VALUES (" + userId + ", " + modeId + ", " + isLibrary + ", " + cardListId + ")");
    }

    public void updateUserCardList(int userCardListId, String name, List<NewCardListCard> cards) {
        UserCardList userCardList = getUserCardList(userCardListId);
        cardListService.updateCardList(userCardList.getCardList().getId(), name, cards);
    }

    public void deleteUserCardList(int userCardListId) {
        UserCardList userCardList = getUserCardList(userCardListId);
        database.transaction(() -> {
            database.execute("DELETE FROM user_card_list WHERE id = " + userCardListId);
            cardListService.deleteCardList(userCardList.getCardList().getId());
            return null;
        });
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

    private UserCardList mapUserList(QueryResult result) {
        int id = result.getInteger("id");
        Mode mode = modeService.getMode(result.getInteger("mode_id"));
        boolean library = result.getBoolean("library");

        CardList cardList;
        // Developer has all cards
        int userId = result.getInteger("user_id");
        if (library && (userId == 1)) {
            cardList = createCompleteCardList();
        } else {
            cardList = cardListService.getCardList(result.getInteger("card_list_id"));
        }

        return new UserCardList(id, mode, library, cardList);
    }

    private CardList createCompleteCardList() {
        LinkedList<CardListCard> cards = new LinkedList<>();
        for (Card card : cardService.getCards()) {
            for (Foil foil : foilService.getFoils()) {
                cards.add(new CardListCard(0, card, foil, 999));
            }
        }
        return new CardList(0, "Complete", cards);
    }
}
