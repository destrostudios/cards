package amara.libraries.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public abstract class Database {

    public Database(String path, String user, String password) {
        this.path = path;
        this.user = user;
        this.password = password;
    }
    private String path;
    private String user;
    private String password;
    private Connection connection;

    public void transaction(Runnable runnable) {
        try {
            ensureConnection();
            connection.setAutoCommit(false);
            runnable.run();
            connection.commit();
        } catch (Exception ex) { // Make sure to catch ALL exceptions, not only SQLException
            try {
                connection.rollback();
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public QueryResult select(String query) {
        try {
            ensureConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return new QueryResult(statement, resultSet);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public QueryResult insert(String query) {
        try {
            ensureConnection();
            Statement statement = connection.createStatement();
            statement.execute(query, Statement.RETURN_GENERATED_KEYS);
            return new QueryResult(statement, statement.getGeneratedKeys());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void execute(String query) {
        try {
            ensureConnection();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void ensureConnection() throws SQLException {
        if ((connection != null) && !connection.isValid(5)) {
            System.out.println("Connection closed, trying to reconnect.");
            connection = null;
        }
        if (connection == null) {
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(getConnectionUrl(path), user, password);
            System.out.println("Connected to database.");
        }
    }

    protected abstract String getConnectionUrl(String path);

    public void close() {
        try {
            connection.close();
            System.out.println("Connection to database closed.");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getEscapedNow() {
        return escape(LocalDateTime.now().toString());
    }

    public String escapeNullable(String text) {
        return ((text != null) ? "'" + escape(text) + "'" : "NULL");
    }

    public String escape(String text) {
        return text
            // Replace single backslash with double backslash;
            .replaceAll("\\\\", "\\\\")
            // Escape single quote
            .replaceAll("'", "\\\\'");
    }
}
