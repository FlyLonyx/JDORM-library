package fr.flylonyx.library.annotations;

import java.lang.annotation.*;

/**
 * Annotation for defining a column name in a database table.
 * The name attribute specifies the column name to be used in mappings.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();
}
