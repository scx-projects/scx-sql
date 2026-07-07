package dev.scx.sql.schema;

/// 数据类型
///
/// @author scx567888
public interface DataType {

    DataTypeKind kind();

    Integer length();

}
