package fr.flylonyx.orm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String url = "jdbc:mysql://";
    private static String host = "localhost";
    private static String database = "db_name";
    private static String user = "root";
    private static String password = "";

    private static Connection connection;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 3000;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            int retries = 0;
            while (retries < MAX_RETRIES) {
                try {
                    connection = DriverManager.getConnection(url + host + "/" + database, user, password);
                    break;
                } catch (SQLException e) {
                    retries++;
                    if (retries == MAX_RETRIES) {
                        throw new SQLException("Unable to connect to the database after " + MAX_RETRIES + " attempts.", e);
                    }
                    try {
                        Thread.sleep(RETRY_DELAY);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return connection;
    }
}
