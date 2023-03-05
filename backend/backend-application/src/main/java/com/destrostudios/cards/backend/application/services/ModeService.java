package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.cards.shared.model.Mode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ModeService {

    public ModeService(Database database) {
        this.database = database;
        modes = new HashMap<>();
        loadModes();
    }
    private Database database;
    private HashMap<Integer, Mode> modes;

    public void loadModes() {
        try (QueryResult result = database.select("SELECT * FROM mode")) {
            while (result.next()) {
                Mode mode = new Mode(result.getInteger("id"), result.getString("name"));
                modes.put(mode.getId(), mode);
            }
        }
    }

    public List<Mode> getModes() {
        return new LinkedList<>(modes.values());
    }

    public Mode getMode(int modeId) {
        return modes.get(modeId);
    }
}
