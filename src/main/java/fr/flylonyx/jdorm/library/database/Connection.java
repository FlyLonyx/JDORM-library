package fr.flylonyx.jdorm.library.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private static String url;
    private static String host;
    private static String database;
    private static String user;
    private static String password;

    private static java.sql.Connection connection;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 3000;

    /**
     * Configures the database connection parameters.
     *
     * @param dbUrl the URL of the database
     * @param dbHost the host of the database
     * @param dbName the name of the database
     * @param dbUser the username for the database connection
     * @param dbPassword the password for the database connection
     */
    public static void configure(String dbUrl, String dbHost, String dbName, String dbUser, String dbPassword) {
        url = dbUrl;
        host = dbHost;
        database = dbName;
        user = dbUser;
        password = dbPassword;
    }

    /**
     * Retrieves a database connection. If the connection is not established or is closed,
     * it attempts to establish a connection using the configured parameters. It retries
     * connecting a maximum number of times with a delay between each retry.
     *
     * @return A java.sql.Connection object representing the established database connection.
     * @throws SQLException if the database connection parameters are not configured,
     * if unable to connect to the database after the maximum number of retries, or
     * if an SQL exception occurs during the connection establishment process.
     */
    public static java.sql.Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (url == null || host == null || database == null || user == null || password == null) {
                throw new SQLException("Database connection parameters are not configured.");
            }

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
