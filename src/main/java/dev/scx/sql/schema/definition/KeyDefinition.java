package dev.scx.sql.schema.definition;

import dev.scx.sql.schema.Key;

/// 用于手动编写 Key
///
/// @author scx567888
/// @version 0.0.1
public class KeyDefinition implements Key {

    private String name;
    private String columnName;
    private boolean primary;

    public KeyDefinition() {

    }

    public KeyDefinition(Key oldKey) {
        setName(oldKey.name());
        setColumnName(oldKey.columnName());
        setPrimary(oldKey.primary());
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
    public boolean primary() {
        return primary;
    }

    public KeyDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public KeyDefinition setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public KeyDefinition setPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

}
