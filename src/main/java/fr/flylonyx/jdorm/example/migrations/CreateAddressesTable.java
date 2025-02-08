package fr.flylonyx.jdorm.example.migrations;

import fr.flylonyx.jdorm.library.core.Schema;
import fr.flylonyx.jdorm.library.core.Table;

public class CreateAddressesTable {
    public static void up(Schema schema) throws Exception {
        Table table = new Table()
                .intColumn("id", true, true)  // PRIMARY KEY
                .varchar("street", 255)
                .varchar("city", 255)
                .intColumn("person_id", false, false)  // Clé étrangère
                .foreignKey("person_id", "persons", "id");

        schema.create("addresses", table);
    }

    public static void down(Schema schema) throws Exception {
        schema.drop("addresses");
    }
}
