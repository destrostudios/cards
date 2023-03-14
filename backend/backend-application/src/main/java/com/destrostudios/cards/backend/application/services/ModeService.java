package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.ModeDeck;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ModeService {

    public ModeService(Database database, CardListService cardListService) {
        this.database = database;
        this.cardListService = cardListService;
        modes = new HashMap<>();
        loadModes();
    }
    private Database database;
    private CardListService cardListService;
    private HashMap<Integer, Mode> modes;

    public void loadModes() {
        try (QueryResult result = database.select("SELECT * FROM mode")) {
            while (result.next()) {
                int modeId = result.getInteger("id");
                Mode mode = new Mode(
                    modeId,
                    result.getString("name"),
                    result.getString("title"),
                    result.getBoolean("has_user_library"),
                    getModeDecks(modeId)
                );
                modes.put(mode.getId(), mode);
            }
        }
    }

    private List<ModeDeck> getModeDecks(int modeId) {
        LinkedList<ModeDeck> modeDecks = new LinkedList<>();
        try (QueryResult result = database.select("SELECT * FROM mode_deck WHERE mode_id = " + modeId)) {
            while (result.next()) {
                modeDecks.add(new ModeDeck(
                    result.getInteger("id"),
                    result.getInteger("mode_id"),
                    cardListService.getCardList(result.getInteger("deck_card_list_id"))
                ));
            }
        }
        return modeDecks;
    }

    public List<Mode> getModes() {
        return new LinkedList<>(modes.values());
    }

    public Mode getMode(int modeId) {
        return modes.get(modeId);
    }

    public Mode getMode(String name) {
        return modes.values().stream().filter(mode -> mode.getName().equals(name)).findFirst().orElseThrow();
    }
}
