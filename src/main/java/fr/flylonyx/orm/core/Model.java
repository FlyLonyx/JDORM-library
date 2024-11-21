package fr.flylonyx.orm.core;

import fr.flylonyx.orm.annotations.Column;
import fr.flylonyx.orm.annotations.Table;
import fr.flylonyx.orm.database.DatabaseConnection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Model {

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

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            int i = 1;
            for (Object value : fields.values()) {
                stmt.setObject(i++, value);
            }
            stmt.setInt(i, (Integer) fields.get("id"));
            stmt.executeUpdate();
        }
    }

    public void delete() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Table table = this.getClass().getAnnotation(Table.class);
        if (table == null) throw new SQLException("Model must have a @Table annotation");

        String tableName = table.name();
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            Field idField = this.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            stmt.setInt(1, (int) idField.get(this));
            stmt.executeUpdate();
        }
    }

    public static <T extends Model> T findById(int id, Class<T> clazz) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) throw new SQLException("Model must have a @Table annotation");

        String tableName = table.name();
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
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

    public static <T extends Model> QueryBuilder<T> query(Class<T> clazz) {
        return new QueryBuilder<>(clazz);
    }
}
