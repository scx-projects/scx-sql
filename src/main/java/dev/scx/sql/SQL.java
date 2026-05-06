package dev.scx.sql;

/// SQL
///
/// @author scx567888
/// @version 0.0.1
public interface SQL {

    static SQL sql(String sql, Object... params) {
        return new DefaultSQL(sql, params);
    }

    /// SQL 语句 (问号形式 '?' 占位)
    String sql();

    Object[] params();

}
