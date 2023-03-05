package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.model.UserCardList;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class UserService {

    private Database database;
    private ModeService modeService;
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
            createUserLibraries(user);
            return user;
        });
    }

    private void createUserLibraries(User user) {
        for (Mode mode : modeService.getModes()) {
            try (QueryResult result = database.insert("INSERT INTO card_list () VALUES ()")) {
                result.next();
                int cardListId = result.getInteger(1);
                database.execute("INSERT INTO user_card_list (user_id, mode_id, library, card_list_id) VALUES (" + user.getId() + ", " + mode.getId() + ", TRUE, " + cardListId + ")");
            }
        }
    }

    public List<UserCardList> getUserCardLists(int userId) {
        LinkedList<UserCardList> userCardLists = new LinkedList<>();
        try (QueryResult result = database.select("SELECT * FROM user_card_list WHERE user_id = " + userId)) {
            while (result.next()) {
                int id = result.getInteger("id");
                Mode mode = modeService.getMode(result.getInteger("mode_id"));
                boolean library = result.getBoolean("library");
                CardList cardList = cardListService.getCardList(result.getInteger("card_list_id"));
                UserCardList userCardList = new UserCardList(id, mode, library, cardList);
                userCardLists.add(userCardList);
            }
        }
        return userCardLists;
    }
}
