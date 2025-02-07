package fr.flylonyx.library.core;


import fr.flylonyx.library.annotations.Column;
import fr.flylonyx.library.annotations.Table;
import fr.flylonyx.library.database.Connection;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder<T extends Model> {
    private final Class<T> clazz;
    private final StringBuilder query;
    private final List<Object> parameters = new ArrayList<>();

    public QueryBuilder(Class<T> clazz) {
        this.clazz = clazz;
        String tableName = clazz.getAnnotation(Table.class).name();
        this.query = new StringBuilder("SELECT * FROM " + tableName);
    }


    public QueryBuilder<T> where(String column, String operator, Object value) {
        query.append(query.toString().contains(" WHERE ") ? " AND " : " WHERE ")
                .append(column).append(" ").append(operator).append(" ?");
        parameters.add(value);
        return this;
    }

    public QueryBuilder<T> orWhere(String column, String operator, Object value) {
        query.append(" OR ").append(column).append(" ").append(operator).append(" ?");
        parameters.add(value);
        return this;
    }

    public QueryBuilder<T> whereBetween(String column, Object min, Object max) {
        query.append(query.toString().contains(" WHERE ") ? " AND " : " WHERE ")
                .append(column).append(" BETWEEN ? AND ?");
        parameters.add(min);
        parameters.add(max);
        return this;
    }

    public QueryBuilder<T> whereIn(String column, List<Object> values) {
        String placeholders = values.stream().map(v -> "?").collect(Collectors.joining(", "));
        query.append(query.toString().contains(" WHERE ") ? " AND " : " WHERE ")
                .append(column).append(" IN (").append(placeholders).append(")");
        parameters.addAll(values);
        return this;
    }

    public QueryBuilder<T> join(String table, String first, String operator, String second) {
        query.append(" JOIN ").append(table)
                .append(" ON ").append(first).append(" ").append(operator).append(" ").append(second);
        return this;
    }

    public QueryBuilder<T> leftJoin(String table, String first, String operator, String second) {
        query.append(" LEFT JOIN ").append(table)
                .append(" ON ").append(first).append(" ").append(operator).append(" ").append(second);
        return this;
    }

    public QueryBuilder<T> rightJoin(String table, String first, String operator, String second) {
        query.append(" RIGHT JOIN ").append(table)
                .append(" ON ").append(first).append(" ").append(operator).append(" ").append(second);
        return this;
    }

    public QueryBuilder<T> groupBy(String column) {
        query.append(" GROUP BY ").append(column);
        return this;
    }

    public QueryBuilder<T> having(String column, String operator, Object value) {
        query.append(" HAVING ").append(column).append(" ").append(operator).append(" ?");
        parameters.add(value);
        return this;
    }

    public QueryBuilder<T> orderBy(String column) {
        query.append(" ORDER BY ").append(column);
        return this;
    }

    public QueryBuilder<T> orderByDesc(String column) {
        query.append(" ORDER BY ").append(column).append(" DESC");
        return this;
    }

    public QueryBuilder<T> limit(int limit) {
        query.append(" LIMIT ").append(limit);
        return this;
    }

    public QueryBuilder<T> offset(int offset) {
        query.append(" OFFSET ").append(offset);
        return this;
    }

    public T first() throws Exception {
        limit(1);
        List<T> results = execute();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<T> execute() throws Exception {
        List<T> results = new ArrayList<>();
        try (Statement stmt = Connection.getConnection().createStatement();
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
