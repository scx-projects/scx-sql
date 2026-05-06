package dev.scx.sql.metadata;

import java.sql.DatabaseMetaData;

/// @see DatabaseMetaData#getCatalogs()
public record CatalogMetadata(String TABLE_CAT) {

}
