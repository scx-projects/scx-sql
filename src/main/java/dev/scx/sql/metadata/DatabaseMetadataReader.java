package dev.scx.sql.metadata;

import dev.scx.sql.dialect.Dialect;
import dev.scx.sql.extractor.ResultSetExtractor;
import dev.scx.sql.schema.Table;
import dev.scx.sql.schema.definition.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static dev.scx.sql.extractor.ResultSetExtractor.ofBeanList;

/// 围绕 JDBC DatabaseMetaData 提供 metadata 读取、metadata 到 definition 的转换，以及表结构装配的能力.
///
/// @author scx567888
/// @version 0.0.1
public final class DatabaseMetadataReader {

    private static final ResultSetExtractor<List<CatalogMetadata>, RuntimeException> CATALOG_METADATA_LIST_EXTRACTOR = ofBeanList(CatalogMetadata.class);
    private static final ResultSetExtractor<List<SchemaMetadata>, RuntimeException> SCHEMA_METADATA_LIST_EXTRACTOR = ofBeanList(SchemaMetadata.class);
    private static final ResultSetExtractor<List<TableMetadata>, RuntimeException> TABLE_METADATA_LIST_EXTRACTOR = ofBeanList(TableMetadata.class);
    private static final ResultSetExtractor<List<ColumnMetadata>, RuntimeException> COLUMN_METADATA_LIST_EXTRACTOR = ofBeanList(ColumnMetadata.class);
    private static final ResultSetExtractor<List<KeyMetadata>, RuntimeException> KEY_METADATA_LIST_EXTRACTOR = ofBeanList(KeyMetadata.class);
    private static final ResultSetExtractor<List<IndexMetadata>, RuntimeException> INDEX_METADATA_LIST_EXTRACTOR = ofBeanList(IndexMetadata.class);

    // *************************** 核心读取方法 **********************************

    public static List<CatalogMetadata> readCatalogMetadataList(DatabaseMetaData metaData) throws SQLException {
        try (var rs = metaData.getCatalogs()) {
            return CATALOG_METADATA_LIST_EXTRACTOR.extract(rs);
        }
    }

    /// schemaPattern 为 null 表示所有.
    public static List<SchemaMetadata> readSchemaMetadataList(DatabaseMetaData metaData, String catalog, String schemaPattern) throws SQLException {
        try (var rs = metaData.getSchemas(catalog, schemaPattern)) {
            return SCHEMA_METADATA_LIST_EXTRACTOR.extract(rs);
        }
    }

    /// tableNamePattern 为 null 表示所有.
    public static List<TableMetadata> readTableMetadataList(DatabaseMetaData metaData, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        try (var rs = metaData.getTables(catalog, schemaPattern, tableNamePattern, new String[]{"TABLE"})) {
            return TABLE_METADATA_LIST_EXTRACTOR.extract(rs);
        }
    }

    /// columnNamePattern 为 null 表示所有.
    public static List<ColumnMetadata> readColumnMetadataList(DatabaseMetaData metaData, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        try (var rs = metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            return COLUMN_METADATA_LIST_EXTRACTOR.extract(rs);
        }
    }

    public static List<KeyMetadata> readKeyMetadataList(DatabaseMetaData metaData, String catalog, String schema, String table) throws SQLException {
        try (var rs = metaData.getPrimaryKeys(catalog, schema, table)) {
            return KEY_METADATA_LIST_EXTRACTOR.extract(rs);
        }
    }

    public static List<IndexMetadata> readIndexMetadataList(DatabaseMetaData metaData, String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        try (var rs = metaData.getIndexInfo(catalog, schema, table, unique, approximate)) {
            return INDEX_METADATA_LIST_EXTRACTOR.extract(rs);
        }
    }

    // *************************** toXXXDefinition **********************************

    /// 这里只会填充基本信息.
    public static TableDefinition toTableDefinition(TableMetadata tableMetadata) {
        return new TableDefinition()
            .setCatalog(tableMetadata.TABLE_CAT())
            .setSchema(tableMetadata.TABLE_SCHEM())
            .setName(tableMetadata.TABLE_NAME())
            .setComment(tableMetadata.REMARKS());
    }

    public static ColumnDefinition toColumnDefinition(ColumnMetadata columnMetadata, Dialect dialect) {
        var dataType = toDataTypeDefinition(columnMetadata, dialect);

        var notNull = "NO".equals(columnMetadata.IS_NULLABLE());
        var autoincrement = "YES".equals(columnMetadata.IS_AUTOINCREMENT());

        return new ColumnDefinition()
            .setName(columnMetadata.COLUMN_NAME())
            .setDataType(dataType)
            .setDefaultValue(columnMetadata.COLUMN_DEF())
            .setOnUpdate(null)
            .setNotNull(notNull)
            .setAutoIncrement(autoincrement)
            .setComment(columnMetadata.REMARKS());
    }

    public static DataTypeDefinition toDataTypeDefinition(ColumnMetadata columnMetadata, Dialect dialect) {
        // 使用方言归一化类型.
        return new DataTypeDefinition()
            .setKind(dialect.dialectTypeNameToDataTypeKind(columnMetadata.TYPE_NAME()))
            .setLength(columnMetadata.COLUMN_SIZE());
    }

    public static KeyDefinition toKeyDefinition(KeyMetadata keyMetadata) {
        return new KeyDefinition()
            .setName(keyMetadata.PK_NAME())
            .setColumnName(keyMetadata.COLUMN_NAME())
            .setPrimary(true);
    }

    public static IndexDefinition toIndexDefinition(IndexMetadata indexMetadata) {
        return new IndexDefinition()
            .setName(indexMetadata.INDEX_NAME())
            .setColumnName(indexMetadata.COLUMN_NAME())
            .setUnique(!indexMetadata.NON_UNIQUE());
    }

    // *************************** 高级加载方法 **********************************

    /// 完整加载表
    public static Table loadTable(DatabaseMetaData metaData, String catalog, String schema, String tableName, Dialect dialect) throws SQLException {
        var tableMetadataList = readTableMetadataList(metaData, catalog, schema, tableName);
        // 没有表
        if (tableMetadataList.isEmpty()) {
            return null;
        }
        // 找到多个表
        if (tableMetadataList.size() > 1) {
            throw new IllegalStateException("found multiple tables: " + tableName);
        }
        var tableMetadata = tableMetadataList.get(0);

        // 填充基本信息
        var tableDefinition = toTableDefinition(tableMetadata);

        // 加载 列
        var columnMetadataList = readColumnMetadataList(metaData, tableDefinition.catalog(), tableDefinition.schema(), tableDefinition.name(), null);
        // 加载 Key
        var keyMetadataList = readKeyMetadataList(metaData, tableDefinition.catalog(), tableDefinition.schema(), tableDefinition.name());
        // 加载 Index
        var indexMetadataList = readIndexMetadataList(metaData, tableDefinition.catalog(), tableDefinition.schema(), tableDefinition.name(), false, false);

        // 填充 列, Key, Index
        for (var columnMetadata : columnMetadataList) {
            // 添加列
            tableDefinition.addColumn(toColumnDefinition(columnMetadata, dialect));
        }

        for (var keyMetadata : keyMetadataList) {
            tableDefinition.addKey(toKeyDefinition(keyMetadata));
        }

        for (var indexMetadata : indexMetadataList) {
            tableDefinition.addIndex(toIndexDefinition(indexMetadata));
        }

        return tableDefinition;
    }

    /// 在当前上下文中 完整加载表, 不会关闭 Connection.
    public static Table loadCurrentTable(Connection connection, String tableName, Dialect dialect) throws SQLException {
        return loadTable(connection.getMetaData(), connection.getCatalog(), connection.getSchema(), tableName, dialect);
    }

}
