package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.CardListCard;
import com.destrostudios.cards.shared.model.Foil;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

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
}
