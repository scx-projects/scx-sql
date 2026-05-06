package dev.scx.sql.schema.definition;

import dev.scx.sql.schema.Column;
import dev.scx.sql.schema.DataType;

/// 用于手动编写 Column
///
/// @author scx567888
/// @version 0.0.1
public class ColumnDefinition implements Column {

    private String name;
    private DataTypeDefinition dataType;
    private String defaultValue;
    private String onUpdate;
    private boolean notNull;
    private boolean autoIncrement;
    private String comment;

    public ColumnDefinition() {

    }

    public ColumnDefinition(Column oldColumn) {
        setName(oldColumn.name());
        setDataType(oldColumn.dataType());
        setDefaultValue(oldColumn.defaultValue());
        setOnUpdate(oldColumn.onUpdate());
        setNotNull(oldColumn.notNull());
        setAutoIncrement(oldColumn.autoIncrement());
        setComment(oldColumn.comment());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public DataTypeDefinition dataType() {
        return dataType;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String onUpdate() {
        return onUpdate;
    }

    @Override
    public boolean notNull() {
        return notNull;
    }

    @Override
    public boolean autoIncrement() {
        return autoIncrement;
    }

    @Override
    public String comment() {
        return comment;
    }

    public ColumnDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public ColumnDefinition setDataType(DataType dataType) {
        this.dataType = new DataTypeDefinition(dataType);
        return this;
    }

    public ColumnDefinition setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ColumnDefinition setOnUpdate(String onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    public ColumnDefinition setNotNull(boolean notNull) {
        this.notNull = notNull;
        return this;
    }

    public ColumnDefinition setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }

    public ColumnDefinition setComment(String comment) {
        this.comment = comment;
        return this;
    }

}
