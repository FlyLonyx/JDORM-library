package fr.flylonyx.jdorm.library.annotations;

import java.lang.annotation.*;

/**
 * Annotation to declare a table name for a class representation in a database.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String name();
}
