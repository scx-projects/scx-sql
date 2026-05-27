package dev.scx.sql.metadata;

import java.sql.DatabaseMetaData;

/// @see DatabaseMetaData#getSchemas(String, String)
public record SchemaMetadata(String TABLE_SCHEM,
                             String TABLE_CATALOG) {

}
