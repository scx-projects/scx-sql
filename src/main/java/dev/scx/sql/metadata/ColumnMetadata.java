package dev.scx.sql.metadata;

import java.sql.DatabaseMetaData;

/// @see DatabaseMetaData#getColumns(String, String, String, String)
public record ColumnMetadata(String TABLE_CAT,
                             String TABLE_SCHEM,
                             String TABLE_NAME,
                             String COLUMN_NAME,
                             int DATA_TYPE,
                             String TYPE_NAME,
                             int COLUMN_SIZE,
                             String BUFFER_LENGTH,//is not used.
                             int DECIMAL_DIGITS,
                             int NUM_PREC_RADIX,
                             int NULLABLE,
                             String REMARKS,
                             String COLUMN_DEF,
                             int SQL_DATA_TYPE,//unused
                             int SQL_DATETIME_SUB,//unused
                             int CHAR_OCTET_LENGTH,
                             int ORDINAL_POSITION,
                             String IS_NULLABLE,
                             String SCOPE_CATALOG,
                             String SCOPE_SCHEMA,
                             String SCOPE_TABLE,
                             short SOURCE_DATA_TYPE,
                             String IS_AUTOINCREMENT,
                             String IS_GENERATEDCOLUMN) {

}
