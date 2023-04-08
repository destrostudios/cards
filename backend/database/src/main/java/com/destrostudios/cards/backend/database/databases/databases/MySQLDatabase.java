package com.destrostudios.cards.backend.database.databases.databases;

import com.destrostudios.cards.backend.database.databases.Database;

public class MySQLDatabase extends Database {

    public MySQLDatabase(String path, String user, String password) {
        super(path, user, password);
    }

    @Override
    protected String getConnectionUrl(String path) {
        return "jdbc:mysql:" + path + "?autoReconnect=true&serverTimezone=UTC";
    }
}
