package fr.flylonyx.library.core;


import fr.flylonyx.library.database.Connection;
import lombok.NonNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MigrationManager {
    private static final String MIGRATIONS_TABLE = "migrations_test";

    /**
     * Initializes the migrations table by creating it if it does not already exist.
     * This method uses the existing database connection obtained from Connection.getConnection().
     * An SQL exception may be thrown if there is an issue executing the create table command.
     *
     * @throws SQLException if an error occurs during table creation.
     */
    public static void initialize() throws SQLException {
        try (Statement statement = Connection.getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS " + MIGRATIONS_TABLE + " (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "migration_name VARCHAR(255) NOT NULL, " +
                    "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }
    }

    /**
     * Retrieves a list of executed migration names from the database.
     *
     * @return A list of strings representing the names of executed migrations.
     * @throws SQLException If an SQL exception occurs while accessing the database.
     */
    public static List<String> getExecutedMigrations() throws SQLException {
        List<String> executedMigrations = new ArrayList<>();
        String query = "SELECT migration_name FROM " + MIGRATIONS_TABLE;

        try (Statement stmt = Connection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                executedMigrations.add(rs.getString("migration_name"));
            }
        }
        return executedMigrations;
    }

    /**
     * Applies a migration with the given migration name and runnable action.
     * If the migration has already been executed, it will be skipped.
     *
     * @param migrationName The name of the migration to apply.
     * @param upMigration The runnable action representing the migration to be applied.
     * @throws SQLException If an error occurs while applying the migration.
     */
    public static void applyMigration(@NonNull String migrationName, @NonNull Runnable upMigration) throws SQLException {
        if (isMigrationExecuted(migrationName)) {
            System.out.println("Migration " + migrationName + " already applied. Skipping...");
            return;
        }

        try {
            upMigration.run();
            logMigration(migrationName);
            System.out.println("Migration " + migrationName + " applied successfully.");
        } catch (Exception e) {
            System.err.println("Error applying migration " + migrationName + ": " + e.getMessage());
            throw new SQLException("Failed to apply migration " + migrationName, e);
        }
    }

    /**
     * Rollback a migration by name using the provided downMigration logic.
     *
     * @param migrationName The name of the migration to rollback.
     * @param downMigration The down migration logic to rollback the migration.
     * @throws SQLException If an SQL exception occurs during the rollback process.
     */
    public static void rollbackMigration(@NonNull String migrationName, @NonNull Runnable downMigration) throws SQLException {
        if (!isMigrationExecuted(migrationName)) {
            System.out.println("Migration " + migrationName + " was not applied. Skipping rollback...");
            return;
        }
        try {
            downMigration.run();
            removeMigrationLog(migrationName);
            System.out.println("Migration " + migrationName + " rolled back successfully.");
        } catch (Exception e) {
            System.err.println("Error rolling back migration " + migrationName + ": " + e.getMessage());
            throw new SQLException("Failed to rollback migration " + migrationName, e);
        }
    }

    /**
     * Checks if a migration with the given name has been executed.
     *
     * @param migrationName the name of the migration to check if executed
     * @return true if the migration has been executed, false otherwise
     * @throws SQLException if an SQL exception occurs during the execution
     */
    private static boolean isMigrationExecuted(@NonNull String migrationName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + MIGRATIONS_TABLE + " WHERE migration_name = ?";
        try (PreparedStatement stmt = Connection.getConnection().prepareStatement(query)) {
            stmt.setString(1, migrationName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Logs a migration by inserting the migration name into the migrations table.
     *
     * @param migrationName The name of the migration to be logged
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    private static void logMigration(@NonNull String migrationName) throws SQLException {
        String query = "INSERT INTO " + MIGRATIONS_TABLE + " (migration_name) VALUES (?)";
        try (PreparedStatement stmt = Connection.getConnection().prepareStatement(query)) {
            stmt.setString(1, migrationName);
            stmt.executeUpdate();
        }
    }

    /**
     * Removes the migration log entry for the specified migration name.
     * This method deletes the entry from the database table MIGRATIONS_TABLE.
     *
     * @param migrationName The name of the migration to remove from the log
     * @throws SQLException if a database error occurs during the deletion process
     */
    private static void removeMigrationLog(@NonNull String migrationName) throws SQLException {
        String query = "DELETE FROM " + MIGRATIONS_TABLE + " WHERE migration_name = ?";
        try (PreparedStatement stmt = Connection.getConnection().prepareStatement(query)) {
            stmt.setString(1, migrationName);
            stmt.executeUpdate();
        }
    }
}
