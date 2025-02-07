package fr.flylonyx.app.migrations;

import fr.flylonyx.orm.core.Schema;

public class AddEmailToPersons {
    public static void up(Schema schema) throws Exception {
        schema.addColumn("persons", "email", "VARCHAR(255)");
    }

    public static void down(Schema schema) throws Exception {
        schema.dropColumn("persons", "email");
    }
}
