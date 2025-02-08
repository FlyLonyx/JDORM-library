package fr.flylonyx.library.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ColumnType {
    INT("INT"),
    BIGINT("BIGINT"),
    SMALLINT("SMALLINT"),
    TINYINT("TINYINT"),
    DECIMAL("DECIMAL"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    VARCHAR("VARCHAR"),
    CHAR("CHAR"),
    TEXT("TEXT"),
    LONGTEXT("LONGTEXT"),
    BOOLEAN("BOOLEAN"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    TIMESTAMP("TIMESTAMP"),
    TIME("TIME"),
    BLOB("BLOB"),
    UUID("VARCHAR(36)");

    private final String baseType;


    public String formatType(Integer length, Integer precision, Integer scale) {
        if (this == VARCHAR || this == CHAR) {
            return baseType + "(" + length + ")";
        }
        if (this == DECIMAL && precision != null && scale != null) {
            return baseType + "(" + precision + ", " + scale + ")";
        }
        return baseType;
    }
}
