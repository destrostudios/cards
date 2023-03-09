package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.changes.NewCardListCard;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CardListService {

    private Database database;
    private CardService cardService;
    private FoilService foilService;

    public CardList getCardList(int cardListId) {
        try (QueryResult result = database.select("SELECT * FROM card_list WHERE id = " + cardListId)) {
            if (!result.next()) {
                throw new RuntimeException("CardList #" + cardListId + " not found.");
            }
            String name = result.getString("name");
            return new CardList(cardListId, name, getCards(cardListId));
        }
    }

    private List<CardListCard> getCards(int cardListId) {
        LinkedList<CardListCard> cards = new LinkedList<>();
        try (QueryResult result = database.select("SELECT * FROM card_list_card WHERE card_list_id = " + cardListId)) {
            while (result.next()) {
                int id = result.getInteger("id");
                Card card = cardService.getCard(result.getInteger("card_id"));
                Foil foil = foilService.getFoil(result.getInteger("foil_id"));
                int amount = result.getInteger("amount");
                cards.add(new CardListCard(id, card, foil, amount));
            }
        }
        return cards;
    }

    public int createCardList() {
        try (QueryResult result = database.insert("INSERT INTO card_list (name) VALUES ()")) {
            result.next();
            return result.getInteger(1);
        }
    }

    public void updateCardList(int cardListId, String name, List<NewCardListCard> cards) {
        database.transaction(() -> {
            database.execute("UPDATE card_list_card SET name = '" + database.escape(name) + "' WHERE card_list_id = " + cardListId);
            clearCardListCards(cardListId);
            database.execute(
                "INSERT INTO card_list_card (card_list_id, card_id, foil_id, amount) VALUES " +
                cards.stream()
                    .map(card -> "(" + cardListId + ", " + card.getCardId() + ", " + card.getFoilId() + ", " + card.getAmount() + ")")
                    .collect(Collectors.joining(","))
            );
            return null;
        });
    }

    public void addCard(int cardListId, int cardId, int foilId, int amount) {
        try (QueryResult result = database.select("SELECT id, amount FROM card_list_card WHERE card_list_id = " + cardListId + " AND card_id = " + cardId + " AND foil_id = " + foilId)) {
            if (result.next()) {
                int id = result.getInteger("id");
                int oldAmount = result.getInteger("amount");
                int newAmount = (oldAmount + amount);
                database.execute("UPDATE card_list_card SET amount = " + newAmount + " WHERE id = " + id);
            } else {
                database.execute("INSERT INTO card_list_card (card_list_id, card_id, foil_id, amount) VALUES (" + cardListId + ", " + cardId + ", " + foilId + ", " + amount + ")");
            }
        }
    }

    public void deleteCardList(int cardListId) {
        clearCardListCards(cardListId);
        database.execute("DELETE FROM card_list WHERE id = " + cardListId);
    }

    private void clearCardListCards(int cardListId) {
        database.execute("DELETE FROM card_list_card WHERE card_list_id = " + cardListId);
    }
}
