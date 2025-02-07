package fr.flylonyx.app.migrations;


import fr.flylonyx.orm.core.Schema;
import fr.flylonyx.orm.core.Table;

public class CreatePersonsTable {
    public static void up(Schema schema) throws Exception {
        Table table = new Table()
                .intColumn("id", true, true)
                .varchar("first_name", 255)
                .varchar("last_name", 255)
                .intColumn("age", false, false);

        schema.create("persons", table);
    }

    public static void down(Schema schema) throws Exception {
        schema.drop("persons");
    }
}
