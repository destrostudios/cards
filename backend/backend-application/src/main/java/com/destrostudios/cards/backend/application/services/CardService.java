package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.backend.database.databases.QueryResult;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.rules.Cards;

import java.util.*;
import java.util.function.Predicate;

public class CardService {

    public CardService(Database database) {
        this.database = database;
        cards = new HashMap<>();
        loadCards();
    }
    private Database database;
    private HashMap<Integer, Card> cards;

    public void loadCards() {
        HashSet<String> missingCardPaths = new HashSet<>(Arrays.asList(Cards.COLLECTIBLE));
        try (QueryResult result = database.select("SELECT * FROM card")) {
            while (result.next()) {
                Card card = new Card(result.getInteger("id"), result.getString("path"), result.getBoolean("core"));
                cards.put(card.getId(), card);
                missingCardPaths.remove(card.getPath());
            }
        }
        for (String path : missingCardPaths) {
            try (QueryResult result = database.insert("INSERT INTO card (path, core) VALUES ('" + path + "', 0)")) {
                result.next();
                int id = result.getInteger(1);
                Card card = new Card(id, path, false);
                cards.put(card.getId(), card);
            }
        }
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards.values());
    }

    public Card getCard(int cardId) {
        return cards.get(cardId);
    }

    public List<Card> getCards_Core() {
        return getCards(Card::isCore);
    }

    public List<Card> getCards_NonCore() {
        return getCards(card -> !card.isCore());
    }

    private List<Card> getCards(Predicate<Card> filter) {
        return cards.values().stream().filter(filter).toList();
    }
}
