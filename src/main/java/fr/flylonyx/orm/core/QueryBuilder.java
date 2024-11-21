package fr.flylonyx.orm.core;


import fr.flylonyx.orm.annotations.Column;
import fr.flylonyx.orm.annotations.Table;
import fr.flylonyx.orm.database.DatabaseConnection;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder<T extends Model> {
    private final Class<T> clazz;
    private final StringBuilder query;

    public QueryBuilder(Class<T> clazz) {
        this.clazz = clazz;
        String tableName = clazz.getAnnotation(Table.class).name();
        this.query = new StringBuilder("SELECT * FROM " + tableName);
    }

    public QueryBuilder<T> orderByDesc(@NonNull String column) {
        query.append(" ORDER BY ").append(column).append(" DESC");
        return this;
    }

    public QueryBuilder<T> limit(int limit) {
        query.append(" LIMIT ").append(limit);
        return this;
    }

    public T first() throws Exception {
        limit(1);
        List<T> results = execute();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<T> execute() throws Exception {
        List<T> results = new ArrayList<>();
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {

            while (rs.next()) {
                T instance = clazz.getDeclaredConstructor().newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Column.class)) {
                       Column column = field.getAnnotation(Column.class);
                        field.setAccessible(true);
                        field.set(instance, rs.getObject(column.name()));
                    }
                }
                results.add(instance);
            }
        }
        return results;
    }
}
