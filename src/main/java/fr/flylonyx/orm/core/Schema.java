package fr.flylonyx.orm.core;

import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Schema {
    private final Connection connection;

    public Schema(Connection connection) {
        this.connection = connection;
    }

    public void create(@NonNull String tableName, @NonNull TableBuilder builder) throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");
        builder.build(sql);
        sql.append(");");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql.toString());
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

    @FunctionalInterface
    public interface TableBuilder {
        void build(StringBuilder sql);
    }
}
