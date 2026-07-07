package dev.scx.sql.metadata;

import java.sql.DatabaseMetaData;

/// @see DatabaseMetaData#getTables(String, String, String, String[])
public record TableMetadata(String TABLE_CAT,
                            String TABLE_SCHEM,
                            String TABLE_NAME,
                            String TABLE_TYPE,
                            String REMARKS,
                            String TYPE_CAT,
                            String TYPE_SCHEM,
                            String TYPE_NAME,
                            String SELF_REFERENCING_COL_NAME,
                            String REF_GENERATION) {

}
