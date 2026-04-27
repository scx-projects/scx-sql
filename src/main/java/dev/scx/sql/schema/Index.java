package dev.scx.sql.schema;

/// 索引 (目前只支持单列)
///
/// @author scx567888
/// @version 0.0.1
public interface Index {

    String name();

    String columnName();

    boolean unique();

}
