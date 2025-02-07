package fr.flylonyx.example.migrations;

import fr.flylonyx.library.core.Schema;
import fr.flylonyx.library.core.Table;

public class CreatePersonsTable {
    public static void up(Schema schema) throws Exception {
        Table table = new Table()
                .intColumn("id", true, true)  // PRIMARY KEY + AUTO_INCREMENT
                .varchar("first_name", 255)
                .varchar("last_name", 255)
                .varchar("email", 255)
                .intColumn("age", false, false)
                .booleanColumn("is_active")
                .decimal("salary", 10, 2)
                .dateColumn("birth_date")
                .timeStamp("created_at");

        schema.create("persons", table);
    }

    public static void down(Schema schema) throws Exception {
        schema.drop("persons");
    }
}
