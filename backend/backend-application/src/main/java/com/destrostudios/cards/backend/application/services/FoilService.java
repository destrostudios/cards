package com.destrostudios.cards.backend.application.services;

import amara.libraries.database.Database;
import amara.libraries.database.QueryResult;
import com.destrostudios.cards.shared.model.Foil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FoilService {

    public FoilService(Database database) {
        this.database = database;
        foils = new HashMap<>();
        loadFoils();
    }
    private Database database;
    private HashMap<Integer, Foil> foils;

    public void loadFoils() {
        try (QueryResult result = database.select("SELECT * FROM foil")) {
            while (result.next()) {
                Foil foil = new Foil(result.getInteger("id"), result.getString("name"));
                foils.put(foil.getId(), foil);
            }
        }
    }

    public List<Foil> getFoils() {
        return new LinkedList<>(foils.values());
    }

    public Foil getFoil(int foilId) {
        return foils.get(foilId);
    }
}
