package com.destrostudios.cards.backend.database.databases;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class QueryResult implements AutoCloseable {

    public QueryResult(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }
    private Statement statement;
    private ResultSet resultSet;
    
    public Boolean nextBoolean_Close() {
        if (next()) {
            boolean value = getBoolean(1);
            close();
            return value;
        }
        return null;
    }
    
    public Integer nextInteger_Close() {
        if (next()) {
            int value = getInteger(1);
            close();
            return value;
        }
        return null;
    }
    
    public Long nextLong_Close() {
        if (next()) {
            long value = getLong(1);
            close();
            return value;
        }
        return null;
    }
    
    public String nextString_Close() {
        if (next()) {
            String value = getString(1);
            close();
            return value;
        }
        return null;
    }
    
    public boolean next() {
        try {
            return resultSet.next();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    // Boolean
    
    public boolean getBoolean(int columnIndex) {
        try {
            return resultSet.getBoolean(columnIndex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public boolean getBoolean(String columnName) {
        try {
            return resultSet.getBoolean(columnName);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Integer
    
    public int getInteger(int columnIndex) {
        try {
            return resultSet.getInt(columnIndex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public int getInteger(String columnName) {
        try {
            return resultSet.getInt(columnName);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    // Long
    
    public long getLong(int columnIndex) {
        try {
            return resultSet.getLong(columnIndex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public long getLong(String columnName) {
        try {
            return resultSet.getLong(columnName);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    // String
    
    public String getString(int columnIndex) {
        try {
            return resultSet.getString(columnIndex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public String getString(String columnName) {
        try {
            return resultSet.getString(columnName);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Array
    
    public Array getArray(int columnIndex) {
        try {
            return resultSet.getArray(columnIndex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public Array getArray(String columnName) {
        try {
            return resultSet.getArray(columnName);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // DateTime

    public LocalDateTime getDateTime(int columnIndex) {
        try {
            return resultSet.getTimestamp(columnIndex).toLocalDateTime();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public LocalDateTime getDateTime(String columnName) {
        try {
            return resultSet.getTimestamp(columnName).toLocalDateTime();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        try {
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
