package dev.scx.sql.schema.definition;

import dev.scx.sql.schema.Index;

/// 用于手动编写 Index
///
/// @author scx567888
/// @version 0.0.1
public class IndexDefinition implements Index {

    private String name;
    private String columnName;
    private boolean unique;

    public IndexDefinition() {

    }

    public IndexDefinition(Index oldIndex) {
        setName(oldIndex.name());
        setColumnName(oldIndex.columnName());
        setUnique(oldIndex.unique());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String columnName() {
        return columnName;
    }

    @Override
    public boolean unique() {
        return unique;
    }

    public IndexDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public IndexDefinition setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public IndexDefinition setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

}
