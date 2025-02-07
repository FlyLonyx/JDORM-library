package fr.flylonyx.library.core;

import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Schema {
    private final Connection connection;

    public Schema(Connection connection) {
        this.connection = connection;
    }

    public void create(@NonNull String tableName, @NonNull Table table) throws SQLException {
        String sql = "CREATE TABLE " + tableName + " (" + table.build() + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void drop(@NonNull String tableName) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS " + tableName);
        }
    }

    public void addColumn(@NonNull String tableName, @NonNull String columnName, @NonNull String columnType) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void dropColumn(@NonNull String tableName, @NonNull String columnName) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addForeignKey(@NonNull String tableName, @NonNull String columnName, @NonNull String referencedTable, @NonNull String referencedColumn) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " ADD CONSTRAINT fk_" + tableName + "_" + columnName +
                " FOREIGN KEY (" + columnName + ") REFERENCES " + referencedTable + "(" + referencedColumn + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void dropForeignKey(@NonNull String tableName, @NonNull String foreignKeyName) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " DROP FOREIGN KEY " + foreignKeyName;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}
