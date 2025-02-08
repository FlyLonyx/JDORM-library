package fr.flylonyx.jdorm.library.core;

import fr.flylonyx.jdorm.library.utils.ColumnType;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Schema {
    private final Connection connection;

    public Schema(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new table in the database with the given table name and structure defined by the provided Table object.
     *
     * @param tableName the name of the table to be created
     * @param table     the Table object representing the structure of the new table
     * @throws SQLException if a database access error occurs
     */
    public void create(@NonNull String tableName, @NonNull Table table) throws SQLException {
        String sql = "CREATE TABLE " + tableName + " (" + table.build() + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Drops the specified table if it exists in the database.
     *
     * @param tableName the name of the table to be dropped
     * @throws SQLException if a database access error occurs
     */
    public void drop(@NonNull String tableName) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS " + tableName);
        }
    }

    /**
     * Adds a new column to the specified table.
     *
     * @param tableName The name of the table to add the column to.
     * @param columnName The name of the new column.
     * @param columnType The data type of the new column.
     * @param length The length of the column (applicable for VARCHAR and CHAR types).
     * @param precision The precision of the column (applicable for DECIMAL type).
     * @param scale The scale of the column (applicable for DECIMAL type).
     * @throws SQLException If an SQL exception occurs during the column addition.
     */
    public void addColumn(@NonNull String tableName, @NonNull String columnName, @NonNull ColumnType columnType, Integer length, Integer precision, Integer scale) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType.formatType(length, precision, scale);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Drop a column from a specific table in the database schema.
     *
     * @param tableName The name of the table from which to drop the column.
     * @param columnName The name of the column to drop from the table.
     * @throws SQLException If a database access error occurs or the SQL statement does not execute successfully.
     */
    public void dropColumn(@NonNull String tableName, @NonNull String columnName) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Adds a foreign key constraint to the specified table.
     *
     * @param tableName The name of the table to add the foreign key to.
     * @param columnName The name of the column in the specified table to create the foreign key on.
     * @param referencedTable The name of the referenced table for the foreign key constraint.
     * @param referencedColumn The name of the referenced column in the referenced table.
     * @throws SQLException If an SQL exception occurs while executing the query.
     */
    public void addForeignKey(@NonNull String tableName, @NonNull String columnName, @NonNull String referencedTable, @NonNull String referencedColumn) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " ADD CONSTRAINT fk_" + tableName + "_" + columnName +
                " FOREIGN KEY (" + columnName + ") REFERENCES " + referencedTable + "(" + referencedColumn + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Drops a foreign key constraint from the specified table.
     *
     * @param tableName The name of the table from which to drop the foreign key.
     * @param foreignKeyName The name of the foreign key constraint to drop.
     * @throws SQLException if a database access error occurs or the SQL statement does not execute successfully.
     */
    public void dropForeignKey(@NonNull String tableName, @NonNull String foreignKeyName) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " DROP FOREIGN KEY " + foreignKeyName;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}
