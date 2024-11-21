package fr.flylonyx.orm.migrations;


import fr.flylonyx.orm.core.Schema;

public class CreatePersonsTable {
    public static void up(Schema schema) throws Exception {
        schema.create("persons", table -> {
            table.append("id INT AUTO_INCREMENT PRIMARY KEY, ");
            table.append("first_name VARCHAR(255), ");
            table.append("last_name VARCHAR(255), ");
            table.append("age INT");
        });
    }

    public static void down(Schema schema) throws Exception {
        schema.drop("persons");
    }
}
