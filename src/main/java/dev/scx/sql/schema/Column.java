package dev.scx.sql.schema;

/// 列
///
/// @author scx567888
/// @version 0.0.1
public interface Column {

    String name();

    DataType dataType();

    /// 默认值 SQL 表达式, 如 `0`、`'abc'`、`CURRENT_TIMESTAMP`
    /// 没有则返回 null
    String defaultValue();

    /// 更新时触发的 SQL 表达式, 如 `CURRENT_TIMESTAMP`
    /// 没有则返回 null
    String onUpdate();

    boolean notNull();

    boolean autoIncrement();

    default String comment() {
        return null;
    }

}
