package fr.flylonyx.example.migrations;

import fr.flylonyx.library.core.Schema;
import fr.flylonyx.library.utils.ColumnType;

public class ModifySalaryColumnInPersons {
    public static void up(Schema schema) throws Exception {
        schema.addColumn("persons", "salary", ColumnType.DECIMAL, null, 12, 2);
    }

    public static void down(Schema schema) throws Exception {
        schema.dropColumn("persons", "salary");
        schema.addColumn("persons", "salary", ColumnType.DECIMAL, null, 10, 2);
    }
}
