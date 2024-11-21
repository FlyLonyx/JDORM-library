package fr.flylonyx.orm.core;


import fr.flylonyx.orm.database.DatabaseConnection;
import lombok.NonNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MigrationManager {
    private static final String MIGRATIONS_TABLE = "migrations_test";

    public static void initialize() throws SQLException {
        try (Statement statement = DatabaseConnection.getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS " + MIGRATIONS_TABLE + " (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "migration_name VARCHAR(255) NOT NULL, " +
                    "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }
    }

    public static List<String> getExecutedMigrations() throws SQLException {
        List<String> executedMigrations = new ArrayList<>();
        String query = "SELECT migration_name FROM " + MIGRATIONS_TABLE;

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                executedMigrations.add(rs.getString("migration_name"));
            }
        }
        return executedMigrations;
    }

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

    private static boolean isMigrationExecuted(@NonNull String migrationName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + MIGRATIONS_TABLE + " WHERE migration_name = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, migrationName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private static void logMigration(@NonNull String migrationName) throws SQLException {
        String query = "INSERT INTO " + MIGRATIONS_TABLE + " (migration_name) VALUES (?)";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, migrationName);
            stmt.executeUpdate();
        }
    }

    private static void removeMigrationLog(@NonNull String migrationName) throws SQLException {
        String query = "DELETE FROM " + MIGRATIONS_TABLE + " WHERE migration_name = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, migrationName);
            stmt.executeUpdate();
        }
    }
}
