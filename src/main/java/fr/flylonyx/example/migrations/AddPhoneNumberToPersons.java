package fr.flylonyx.example.migrations;

import fr.flylonyx.library.core.Schema;

public class AddPhoneNumberToPersons {
    public static void up(Schema schema) throws Exception {
        schema.addColumn("persons", "phone_number", "VARCHAR(20)");
    }

    public static void down(Schema schema) throws Exception {
        schema.dropColumn("persons", "phone_number");
    }
}
