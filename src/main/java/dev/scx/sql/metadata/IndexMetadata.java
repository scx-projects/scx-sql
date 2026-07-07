package dev.scx.sql.metadata;

import java.sql.DatabaseMetaData;

/// @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
public record IndexMetadata(String TABLE_CAT,
                            String TABLE_SCHEM,
                            String TABLE_NAME,
                            boolean NON_UNIQUE,
                            String INDEX_QUALIFIER,
                            String INDEX_NAME,
                            short TYPE,
                            short ORDINAL_POSITION,
                            String COLUMN_NAME,
                            String ASC_OR_DESC,
                            long CARDINALITY,
                            long PAGES,
                            String FILTER_CONDITION) {

}
