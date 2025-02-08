package fr.flylonyx.example.migrations;

import fr.flylonyx.library.core.Schema;
import fr.flylonyx.library.utils.ColumnType;


public class AddPhoneNumberToPersons {
    public static void up(Schema schema) throws Exception {
        schema.addColumn("persons", "phone_number", ColumnType.VARCHAR, 20, null, null);
    }

    public static void down(Schema schema) throws Exception {
        schema.dropColumn("persons", "phone_number");
    }
}
