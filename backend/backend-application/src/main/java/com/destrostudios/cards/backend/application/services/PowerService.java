package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.backend.database.databases.QueryResult;
import com.destrostudios.cards.shared.model.Power;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.Powers;

import java.util.*;

public class PowerService {

    public PowerService(Database database) {
        this.database = database;
        powers = new HashMap<>();
        loadPowers();
    }
    private Database database;
    private HashMap<Integer, Power> powers;

    public void loadPowers() {
        HashSet<String> missingPowerPaths = new HashSet<>(Arrays.asList(Powers.ALL));
        try (QueryResult result = database.select("SELECT * FROM power")) {
            while (result.next()) {
                Power power = new Power(result.getInteger("id"), result.getString("path"));
                powers.put(power.getId(), power);
                missingPowerPaths.remove(power.getPath());
            }
        }
        for (String path : missingPowerPaths) {
            try (QueryResult result = database.insert("INSERT INTO power (path) VALUES ('" + path + "')")) {
                result.next();
                int id = result.getInteger(1);
                Power power = new Power(id, path);
                powers.put(power.getId(), power);
            }
        }
    }

    public List<Power> getPowers() {
        return new ArrayList<>(powers.values());
    }

    public Power getPower(int powerId) {
        return powers.get(powerId);
    }

    public Power getPower_Spark() {
        return getPower(GameConstants.POWER_PATH_SPARK);
    }

    public Power getPower(String path) {
        return powers.values().stream().filter(power -> power.getPath().equals(path)).findFirst().orElseThrow();
    }
}
