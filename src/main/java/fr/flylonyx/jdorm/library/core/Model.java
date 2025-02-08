package fr.flylonyx.jdorm.library.core;

import fr.flylonyx.jdorm.library.annotations.Column;
import fr.flylonyx.jdorm.library.annotations.Table;
import fr.flylonyx.jdorm.library.database.Connection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public abstract class Model {

    /**
     * Saves the current model instance to the database.
     * Retrieves the table name from the class's @Table annotation.
     * Maps fields annotated with @Column to corresponding column names and values.
     * Constructs and executes an SQL INSERT query to save the model to the table.
     * If the table has a generated primary key, sets the generated ID back to the model's id field.
     *
     * @throws SQLException if an SQL exception occurs during the save operation.
     * @throws IllegalAccessException if access to a field is not allowed during reflection.
     * @throws NoSuchFieldException if a field does not exist in the model class.
     */
    public void save() throws SQLException, IllegalAccessException, NoSuchFieldException {
        Table table = this.getClass().getAnnotation(Table.class);
        if (table == null) throw new SQLException("Model must have a @Table annotation");

        String tableName = table.name();
        Map<String, Object> fields = new HashMap<>();

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                field.setAccessible(true);
                fields.put(column.name(), field.get(this));
            }
        }

        String columns = String.join(", ", fields.keySet());
        String values = String.join(", ", fields.values().stream().map(v -> "?").toArray(String[]::new));
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";

        try (PreparedStatement stmt = Connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            for (Object value : fields.values()) {
                stmt.setObject(i++, value);
            }
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Field idField = this.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(this, generatedKeys.getInt(1));
            }
        }
    }


    /**
     * Updates the current model instance in the database.
     * Retrieves the table name from the class's @Table annotation.
     * Maps fields annotated with @Column to corresponding column names and values.
     * Constructs and executes an SQL UPDATE query to update the model in the table based on its id.
     *
     * @throws SQLException if an SQL exception occurs during the update operation.
     * @throws IllegalAccessException if access to a field is not allowed during reflection.
     */
    public void update() throws SQLException, IllegalAccessException {
        Table table = this.getClass().getAnnotation(Table.class);
        if (table == null) throw new SQLException("Model must have a @Table annotation");

        String tableName = table.name();
        Map<String, Object> fields = new HashMap<>();

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                field.setAccessible(true);
                fields.put(column.name(), field.get(this));
            }
        }

        String setClause = String.join(", ", fields.keySet().stream().map(k -> k + " = ?").toArray(String[]::new));
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE id = ?";

        try (PreparedStatement stmt = Connection.getConnection().prepareStatement(sql)) {
            int i = 1;
            for (Object value : fields.values()) {
                stmt.setObject(i++, value);
            }
            stmt.setInt(i, (Integer) fields.get("id"));
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes the current model instance from the database based on its id.
     * Retrieves the table name from the class's @Table annotation.
     * Constructs and executes an SQL DELETE query to remove the model from the table.
     *
     * @throws SQLException if an SQL exception occurs during the delete operation.
     * @throws NoSuchFieldException if the id field does not exist in the model class.
     * @throws IllegalAccessException if access to the id field is not allowed during reflection.
     */
    public void delete() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Table table = this.getClass().getAnnotation(Table.class);
        if (table == null) throw new SQLException("Model must have a @Table annotation");

        String tableName = table.name();
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement stmt = Connection.getConnection().prepareStatement(sql)) {
            Field idField = this.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            stmt.setInt(1, (int) idField.get(this));
            stmt.executeUpdate();
        }
    }

    /**
     * Finds a model instance by its ID from the database.
     *
     * @param id the ID of the model instance to retrieve.
     * @param clazz the class type of the model.
     * @return the model instance found in the database or null if not found.
     * @throws SQLException if an SQL exception occurs during retrieval.
     * @throws NoSuchMethodException if a specified method cannot be found.
     * @throws IllegalAccessException if access to a method is not allowed during reflection.
     * @throws InvocationTargetException if an invoked method throws an exception.
     * @throws InstantiationException if a specified class object cannot be instantiated.
     */
    public static <T extends Model> T findById(int id, Class<T> clazz) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) throw new SQLException("Model must have a @Table annotation");

        String tableName = table.name();
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement stmt = Connection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T instance = clazz.getDeclaredConstructor().newInstance();
                    for (Field field : clazz.getDeclaredFields()) {
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            field.setAccessible(true);
                            field.set(instance, rs.getObject(column.name()));
                        }
                    }
                    return instance;
                }
            }
        }
        return null;
    }

    /**
     * Creates a QueryBuilder instance for the specified model class.
     *
     * @param clazz the class type of the model.
     * @return a new QueryBuilder instance for building queries on the specified model class.
     */
    public static <T extends Model> QueryBuilder<T> query(Class<T> clazz) {
        return new QueryBuilder<>(clazz);
    }
}
