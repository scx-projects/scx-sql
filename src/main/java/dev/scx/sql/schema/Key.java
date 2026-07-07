package dev.scx.sql.schema;

/// 键 (目前只支持单列)
///
/// @author scx567888
public interface Key {

    String name();

    String columnName();

    boolean primary();

}
