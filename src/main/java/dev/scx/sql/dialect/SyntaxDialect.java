package dev.scx.sql.dialect;

/// 语法区别相关 方言
///
/// @author scx567888
/// @version 0.0.1
public interface SyntaxDialect {

    /// 将字段名或表名用数据库对应的转义符包装 (如 MySQL 使用反引号)
    ///
    /// @param identifier 原始字段名或表名
    /// @return 加了转义符的 SQL 标识符
    default String quoteIdentifier(String identifier) {
        return "`" + identifier + "`";
    }

    /// 应用分页
    default String applyLimit(String sql, Long offset, Long limit) {
        if (limit == null) {
            return sql;
        }
        if (offset == null || offset == 0) {
            return sql + " LIMIT " + limit;
        } else {
            return sql + " LIMIT " + offset + "," + limit;
        }
    }

    /// 应用锁
    default String applySharedLock(String sql) {
        return sql + " FOR SHARE";
    }

    /// 应用锁
    default String applyExclusiveLock(String sql) {
        return sql + " FOR UPDATE";
    }

    /// true 表达式
    default String trueExpression() {
        return "TRUE";
    }

    /// false 表达式
    default String falseExpression() {
        return "FALSE";
    }

}
