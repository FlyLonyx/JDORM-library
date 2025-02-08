package fr.flylonyx.library.core;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<String> columns = new ArrayList<>();
    private final List<String> foreignKeys = new ArrayList<>();


    /**
     * Adds an integer column to the table with the given name, optionally specifying if it is auto-incremented and a primary key.
     *
     * @param name the name of the integer column to add
     * @param autoIncrement true if the column should auto-increment, false otherwise
     * @param primaryKey true if the column should be a primary key, false otherwise
     * @return the Table object for method chaining
     */
    public Table intColumn(String name, boolean autoIncrement, boolean primaryKey) {
        String definition = name + " INT";
        if (autoIncrement) definition += " AUTO_INCREMENT";
        if (primaryKey) definition += " PRIMARY KEY";
        columns.add(definition);
        return this;
    }


    /**
     * Adds a column of type BIGINT to the table.
     *
     * @param name the name of the BIGINT column to be added
     * @return returns the Table object with the column added
     */
    public Table bigInt(String name) {
        columns.add(name + " BIGINT");
        return this;
    }

    /**
     * Adds a column with the specified name and type SMALLINT to the Table.
     *
     * @param name the name of the column to be added
     * @return the Table object with the new column added
     */
    public Table smallInt(String name) {
        columns.add(name + " SMALLINT");
        return this;
    }

    /**
     * Adds a column with the specified name and data type TINYINT to the table.
     *
     * @param name the name of the column to be added with data type TINYINT
     * @return the Table object with the new column added
     */
    public Table tinyInt(String name) {
        columns.add(name + " TINYINT");
        return this;
    }

    /**
     * Adds a DECIMAL column to the table with the specified name, precision, and scale.
     *
     * @param name the name of the DECIMAL column
     * @param precision the precision of the DECIMAL column
     * @param scale the scale of the DECIMAL column
     * @return the Table object for method chaining
     */
    public Table decimal(String name, int precision, int scale) {
        columns.add(name + " DECIMAL(" + precision + ", " + scale + ")");
        return this;
    }

    /**
     * Adds a new floating-point column to the table.
     *
     * @param name the name of the column to be added
     * @return the updated Table object with the new column added
     */
    public Table floatColumn(String name) {
        columns.add(name + " FLOAT");
        return this;
    }

    /**
     * Add a new column to the table with a double data type.
     *
     * @param name the name of the column to be added
     * @return the Table object after adding the column
     */
    public Table doubleColumn(String name) {
        columns.add(name + " DOUBLE");
        return this;
    }

    /**
     * Adds a VARCHAR column to the table with the specified name and length.
     *
     * @param name   the name of the VARCHAR column
     * @param length the length of the VARCHAR column
     * @return the Table object to allow method chaining
     */
    public Table varchar(String name, int length) {
        columns.add(name + " VARCHAR(" + length + ")");
        return this;
    }

    /**
     * Adds a character column with the specified name and length to the table.
     *
     * @param name the name of the column to be added
     * @param length the length of the character column
     * @return the Table object with the new column added
     */
    public Table charColumn(String name, int length) {
        columns.add(name + " CHAR(" + length + ")");
        return this;
    }

    /**
     * Add a new column of type TEXT to the table.
     *
     * @param name the name of the column to be added
     * @return the updated Table object with the new column added
     */
    public Table text(String name) {
        columns.add(name + " TEXT");
        return this;
    }

    /**
     * Adds a column with type LONGTEXT to the table.
     *
     * @param name the name of the column to be added as a LONGTEXT type
     * @return the current Table object to allow method chaining
     */
    public Table longText(String name) {
        columns.add(name + " LONGTEXT");
        return this;
    }

    /**
     * Adds a boolean column to the table structure.
     *
     * @param name the name of the boolean column to be added
     * @return the Table object for method chaining
     */
    public Table booleanColumn(String name) {
        columns.add(name + " BOOLEAN");
        return this;
    }

    /**
     * Adds a new column of type DATE to the table.
     *
     * @param name the name of the new DATE column to be added
     * @return a reference to the Table object for method chaining
     */
    public Table dateColumn(String name) {
        columns.add(name + " DATE");
        return this;
    }

    /**
     * Adds a DATETIME column to the table with the specified name.
     *
     * @param name the name of the column to be added (not null)
     * @return the Table object with the DATETIME column added
     */
    public Table dateTime(String name) {
        columns.add(name + " DATETIME");
        return this;
    }

    /**
     * Adds a TIMESTAMP column to the table.
     *
     * @param name the name of the TIMESTAMP column
     * @return the Table object to allow for method chaining
     */
    public Table timeStamp(String name) {
        columns.add(name + " TIMESTAMP");
        return this;
    }

    /**
     * Adds a new time column to the table.
     *
     * @param name the name of the column to be added
     *
     * @return the Table object with the new time column added
     */
    public Table timeColumn(String name) {
        columns.add(name + " TIME");
        return this;
    }

    /**
     * Add a BLOB column to the table.
     *
     * @param name the name of the BLOB column to add
     * @return the Table object to allow method chaining
     */
    public Table blob(String name) {
        columns.add(name + " BLOB");
        return this;
    }

    /**
     * Adds a UUID column to the table.
     *
     * @param name the name of the column
     * @return the Table object with the UUID column added
     */
    public Table uuid(String name) {
        columns.add(name + " VARCHAR(36)");
        return this;
    }

    /**
     * Adds a foreign key constraint to the current table.
     *
     * @param column the column in the current table to create the foreign key constraint on
     * @param referencedTable the table referenced by the foreign key constraint
     * @param referencedColumn the column in the referenced table
     * @return the current Table object after adding the foreign key constraint
     */
    public Table foreignKey(String column, String referencedTable, String referencedColumn) {
        foreignKeys.add("FOREIGN KEY (" + column + ") REFERENCES " + referencedTable + "(" + referencedColumn + ")");
        return this;
    }

    /**
     * Concatenates columns and foreign keys into a single SQL string separated by commas.
     *
     * @return A String containing the columns and foreign keys separated by commas.
     */
    public String build() {
        String sql = String.join(", ", columns);

        if (!foreignKeys.isEmpty()) {
            sql += ", " + String.join(", ", foreignKeys);
        }

        return sql;
    }
}
