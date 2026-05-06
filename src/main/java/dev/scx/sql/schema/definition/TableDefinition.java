package dev.scx.sql.schema.definition;

import dev.scx.sql.schema.Column;
import dev.scx.sql.schema.Index;
import dev.scx.sql.schema.Key;
import dev.scx.sql.schema.Table;

import java.util.LinkedHashMap;
import java.util.Map;

/// 用于手动编写 Table
///
/// @author scx567888
/// @version 0.0.1
public class TableDefinition implements Table {

    private final Map<String, ColumnDefinition> columnMap;
    private final Map<String, KeyDefinition> keyMap;
    private final Map<String, IndexDefinition> indexMap;

    private String catalog;
    private String schema;
    private String name;
    private String comment;

    public TableDefinition() {
        this.columnMap = new LinkedHashMap<>();
        this.keyMap = new LinkedHashMap<>();
        this.indexMap = new LinkedHashMap<>();
    }

    public TableDefinition(Table oldTable) {
        this();
        setCatalog(oldTable.catalog());
        setSchema(oldTable.schema());
        setName(oldTable.name());
        setComment(oldTable.comment());
        for (var column : oldTable.columns()) {
            addColumn(column);
        }
        for (var key : oldTable.keys()) {
            addKey(key);
        }
        for (var index : oldTable.indexes()) {
            addIndex(index);
        }
    }

    @Override
    public String catalog() {
        return catalog;
    }

    @Override
    public String schema() {
        return schema;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ColumnDefinition[] columns() {
        return columnMap.values().toArray(ColumnDefinition[]::new);
    }

    @Override
    public KeyDefinition[] keys() {
        return keyMap.values().toArray(KeyDefinition[]::new);
    }

    @Override
    public IndexDefinition[] indexes() {
        return indexMap.values().toArray(IndexDefinition[]::new);
    }

    @Override
    public ColumnDefinition getColumn(String name) {
        return columnMap.get(name);
    }

    @Override
    public KeyDefinition getKey(String name) {
        return keyMap.get(name);
    }

    @Override
    public IndexDefinition getIndex(String name) {
        return indexMap.get(name);
    }

    @Override
    public String comment() {
        return comment;
    }

    public TableDefinition setCatalog(String catalog) {
        this.catalog = catalog;
        return this;
    }

    public TableDefinition setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public TableDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public TableDefinition addColumn(Column oldColumn) {
        var column = new ColumnDefinition(oldColumn);
        columnMap.put(column.name(), column);
        return this;
    }

    public TableDefinition removeColumn(String name) {
        columnMap.remove(name);
        return this;
    }

    public TableDefinition clearColumns() {
        columnMap.clear();
        return this;
    }

    public TableDefinition addKey(Key oldKey) {
        var key = new KeyDefinition(oldKey);
        keyMap.put(key.name(), key);
        return this;
    }

    public TableDefinition removeKey(String name) {
        keyMap.remove(name);
        return this;
    }

    public TableDefinition clearKeys() {
        keyMap.clear();
        return this;
    }

    public TableDefinition addIndex(Index oldIndex) {
        var index = new IndexDefinition(oldIndex);
        indexMap.put(index.name(), index);
        return this;
    }

    public TableDefinition removeIndex(String name) {
        indexMap.remove(name);
        return this;
    }

    public TableDefinition clearIndexes() {
        indexMap.clear();
        return this;
    }

    public TableDefinition setComment(String comment) {
        this.comment = comment;
        return this;
    }

}
