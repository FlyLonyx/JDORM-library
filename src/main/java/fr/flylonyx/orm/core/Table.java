package fr.flylonyx.orm.core;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<String> columns = new ArrayList<>();
    private final List<String> foreignKeys = new ArrayList<>();


    public Table intColumn(String name, boolean autoIncrement, boolean primaryKey) {
        String definition = name + " INT";
        if (autoIncrement) definition += " AUTO_INCREMENT";
        if (primaryKey) definition += " PRIMARY KEY";
        columns.add(definition);
        return this;
    }


    public Table bigInt(String name) {
        columns.add(name + " BIGINT");
        return this;
    }

    public Table smallInt(String name) {
        columns.add(name + " SMALLINT");
        return this;
    }

    public Table tinyInt(String name) {
        columns.add(name + " TINYINT");
        return this;
    }

    public Table decimal(String name, int precision, int scale) {
        columns.add(name + " DECIMAL(" + precision + ", " + scale + ")");
        return this;
    }

    public Table floatColumn(String name) {
        columns.add(name + " FLOAT");
        return this;
    }

    public Table doubleColumn(String name) {
        columns.add(name + " DOUBLE");
        return this;
    }

    public Table varchar(String name, int length) {
        columns.add(name + " VARCHAR(" + length + ")");
        return this;
    }

    public Table charColumn(String name, int length) {
        columns.add(name + " CHAR(" + length + ")");
        return this;
    }

    public Table text(String name) {
        columns.add(name + " TEXT");
        return this;
    }

    public Table longText(String name) {
        columns.add(name + " LONGTEXT");
        return this;
    }

    public Table booleanColumn(String name) {
        columns.add(name + " BOOLEAN");
        return this;
    }

    public Table dateColumn(String name) {
        columns.add(name + " DATE");
        return this;
    }

    public Table dateTime(String name) {
        columns.add(name + " DATETIME");
        return this;
    }

    public Table timeStamp(String name) {
        columns.add(name + " TIMESTAMP");
        return this;
    }

    public Table timeColumn(String name) {
        columns.add(name + " TIME");
        return this;
    }

    public Table blob(String name) {
        columns.add(name + " BLOB");
        return this;
    }

    public Table uuid(String name) {
        columns.add(name + " VARCHAR(36)");
        return this;
    }

    public Table foreignKey(String column, String referencedTable, String referencedColumn) {
        foreignKeys.add("FOREIGN KEY (" + column + ") REFERENCES " + referencedTable + "(" + referencedColumn + ")");
        return this;
    }

    public String build() {
        String sql = String.join(", ", columns);

        if (!foreignKeys.isEmpty()) {
            sql += ", " + String.join(", ", foreignKeys);
        }

        return sql;
    }
}
