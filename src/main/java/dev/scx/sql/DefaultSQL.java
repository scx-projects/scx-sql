package dev.scx.sql;

/// 默认 SQL 实现
///
/// @author scx567888
public record DefaultSQL(String sql, Object[] params) implements SQL {

}
