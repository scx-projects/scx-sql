package dev.scx.sql.metadata;

import java.sql.DatabaseMetaData;

/// @see DatabaseMetaData#getPrimaryKeys(String, String, String)
public record KeyMetadata(String TABLE_CAT,
                          String TABLE_SCHEM,
                          String TABLE_NAME,
                          String COLUMN_NAME,
                          short KEY_SEQ,
                          String PK_NAME) {

}
