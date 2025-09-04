package fr.flylonyx.jdorm.library.core;


import fr.flylonyx.jdorm.library.annotations.Column;
import fr.flylonyx.jdorm.library.annotations.Table;
import fr.flylonyx.jdorm.library.database.Connection;
import fr.flylonyx.jdorm.library.utils.Operations;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder<T extends Model> {
    private final Class<T> clazz;
    private final StringBuilder query;
    private final List<Object> parameters = new ArrayList<>();

    /**
     * Constructs a QueryBuilder object for the specified entity class.
     *
     * @param clazz the Class representing the entity for which the query builder is being created
     */
    public QueryBuilder(Class<T> clazz) {
        this.clazz = clazz;
        String tableName = clazz.getAnnotation(Table.class).name();
        this.query = new StringBuilder("SELECT * FROM " + tableName);
    }


    /**
     * Adds a WHERE clause to the query with the specified column, operator, and value.
     *
     * @param column   The name of the column to apply the condition on.
     * @param operator The comparison operator to be used in the condition.
     * @param value    The value to compare with for the given column.
     * @return The updated QueryBuilder instance with the new WHERE condition added.
     */
    public QueryBuilder<T> where(String column, Operations operator, Object value) {
        query.append(query.toString().contains(" WHERE ") ? " AND " : " WHERE ")
                .append(column).append(" ").append(operator.getOperations()).append(" ?");
        parameters.add(value);
        return this;
    }

    /**
     * Adds a conditional OR clause to the query builder using the specified column, operator, and value.
     *
     * @param column the column name to apply the condition on.
     * @param operator the operation to perform in the condition (e.g., equals, not equals).
     * @param value the value to compare against in the condition.
     * @return the query builder instance with the OR condition appended.
     */
    public QueryBuilder<T> orWhere(String column, Operations operator, Object value) {
        query.append(" OR ").append(column).append(" ").append(operator.getOperations()).append(" ?");
        parameters.add(value);
        return this;
    }

    /**
     * Adds a BETWEEN clause to the SQL query with the specified column, minimum value, and maximum value.
     *
     * @param column The column to apply the BETWEEN clause on.
     * @param min The minimum value for the BETWEEN clause.
     * @param max The maximum value for the BETWEEN clause.
     * @return The QueryBuilder instance for method chaining.
     */
    public QueryBuilder<T> whereBetween(String column, Object min, Object max) {
        query.append(query.toString().contains(" WHERE ") ? " AND " : " WHERE ")
                .append(column).append(" BETWEEN ? AND ?");
        parameters.add(min);
        parameters.add(max);
        return this;
    }

    /**
     * Adds a WHERE IN clause to the query based on the provided column and list of values.
     *
     * @param column the column name to be used in the WHERE IN clause
     * @param values the list of values to be checked against the column
     * @return the QueryBuilder instance with the WHERE IN clause added
     */
    public QueryBuilder<T> whereIn(String column, List<Object> values) {
        String placeholders = values.stream().map(v -> "?").collect(Collectors.joining(", "));
        query.append(query.toString().contains(" WHERE ") ? " AND " : " WHERE ")
                .append(column).append(" IN (").append(placeholders).append(")");
        parameters.addAll(values);
        return this;
    }

    /**
     * Joins the specified table with the current query based on the provided conditions.
     *
     * @param table the name of the table to join
     * @param first the column name of the first condition
     * @param operator the operation to apply for the join condition (e.g., EQUALS, GREATER_THAN)
     * @param second the column name or value of the second condition
     * @return the QueryBuilder instance for method chaining
     */
    public QueryBuilder<T> join(String table, String first, Operations operator, String second) {
        query.append(" JOIN ").append(table)
                .append(" ON ").append(first).append(" ").append(operator.getOperations()).append(" ").append(second);
        return this;
    }

    /**
     * Performs a left join operation in the SQL query.
     *
     * @param table the name of the table to left join
     * @param first the column or expression on the left side of the join condition
     * @param operator the comparison operator for the join condition (e.g., EQUALS, LESS_THAN)
     * @param second the column or expression on the right side of the join condition
     * @return QueryBuilder instance for method chaining
     */
    public QueryBuilder<T> leftJoin(String table, String first, Operations operator, String second) {
        query.append(" LEFT JOIN ").append(table)
                .append(" ON ").append(first).append(" ").append(operator.getOperations()).append(" ").append(second);
        return this;
    }

    /**
     * Performs a right join operation in the SQL query with the specified table, condition, and operator.
     *
     * @param table the name of the table to right join
     * @param first the left column in the join condition
     * @param operator the operator to apply in the join condition (e.g., "=", ">", "<=")
     * @param second the right column in the join condition
     * @return the QueryBuilder instance for method chaining
     */
    public QueryBuilder<T> rightJoin(String table, String first, Operations operator, String second) {
        query.append(" RIGHT JOIN ").append(table)
                .append(" ON ").append(first).append(" ").append(operator.getOperations()).append(" ").append(second);
        return this;
    }

    /**
     * Appends a GROUP BY clause with the specified column to the query being built.
     *
     * @param column The column name to group by
     * @return This QueryBuilder instance for method chaining
     */
    public QueryBuilder<T> groupBy(String column) {
        query.append(" GROUP BY ").append(column);
        return this;
    }

    /**
     * Sets the condition for the HAVING clause in the query.
     *
     * @param column   The column to apply the condition on.
     * @param operator The operation to apply (e.g., equals, greater than).
     * @param value    The value to compare against.
     * @return The QueryBuilder instance with the HAVING condition applied.
     */
    public QueryBuilder<T> having(String column, Operations operator, Object value) {
        query.append(" HAVING ").append(column).append(" ").append(operator.getOperations()).append(" ?");
        parameters.add(value);
        return this;
    }

    /**
     * Appends an ORDER BY clause to the query based on the specified column.
     *
     * @param column the column to sort the results by
     * @return QueryBuilder<T> a reference to this QueryBuilder instance
     */
    public QueryBuilder<T> orderBy(String column) {
        query.append(" ORDER BY ").append(column);
        return this;
    }

    /**
     * Orders the query result by the specified column in descending order.
     *
     * @param column the column by which to order the query results
     * @return a QueryBuilder instance with the ORDER BY clause appended for descending order
     */
    public QueryBuilder<T> orderByDesc(String column) {
        query.append(" ORDER BY ").append(column).append(" DESC");
        return this;
    }

    /**
     * Sets the limit for the number of records to retrieve in the query.
     *
     * @param limit the maximum number of records to retrieve
     * @return QueryBuilder instance with the limit clause applied
     */
    public QueryBuilder<T> limit(int limit) {
        query.append(" LIMIT ").append(limit);
        return this;
    }

    /**
     * Sets the OFFSET value for the SQL query.
     *
     * @param offset the offset value to set
     * @return QueryBuilder<T> instance with the offset value added to the query
     */
    public QueryBuilder<T> offset(int offset) {
        query.append(" OFFSET ").append(offset);
        return this;
    }

    /**
     * Retrieves the first element from the executed query result.
     *
     * @return the first element from the query result or null if the result is empty
     * @throws Exception if an error occurs during query execution
     */
    public T first() throws Exception {
        limit(1);
        List<T> results = execute();
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Executes the query and maps the result set to a list of objects of type T.
     * Uses the class type T to create instances, retrieves fields annotated with @Column,
     * and maps the database values to the corresponding fields.
     *
     * @return List of objects of type T from the query result set.
     * @throws Exception if an error occurs during query execution or object instantiation.
     */
    public List<T> execute() throws Exception {
        List<T> results = new ArrayList<>();
        try (PreparedStatement stmt = Connection.getConnection().prepareStatement(query.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
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
        }

        return results;
    }
}
