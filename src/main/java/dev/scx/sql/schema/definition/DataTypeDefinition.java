package dev.scx.sql.schema.definition;

import dev.scx.sql.schema.DataType;
import dev.scx.sql.schema.DataTypeKind;

/// 用于手动编写 DataType
///
/// @author scx567888
/// @version 0.0.1
public class DataTypeDefinition implements DataType {

    private DataTypeKind kind;
    private Integer length;

    public DataTypeDefinition() {

    }

    public DataTypeDefinition(DataType oldDataType) {
        setKind(oldDataType.kind());
        setLength(oldDataType.length());
    }

    @Override
    public DataTypeKind kind() {
        return kind;
    }

    @Override
    public Integer length() {
        return length;
    }

    public DataTypeDefinition setKind(DataTypeKind kind) {
        this.kind = kind;
        return this;
    }

    public DataTypeDefinition setLength(Integer length) {
        this.length = length;
        return this;
    }

}
