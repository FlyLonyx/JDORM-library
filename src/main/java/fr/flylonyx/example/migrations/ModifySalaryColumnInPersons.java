package fr.flylonyx.example.migrations;

import fr.flylonyx.library.core.Schema;

public class ModifySalaryColumnInPersons {
    public static void up(Schema schema) throws Exception {
        schema.addColumn("persons", "salary", "DECIMAL(12, 2)");
    }

    public static void down(Schema schema) throws Exception {
        schema.dropColumn("persons", "salary");
        schema.addColumn("persons", "salary", "DECIMAL(10, 2)");
    }
}
