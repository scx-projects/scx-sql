package dev.scx.sql.extractor.bean;

import dev.scx.sql.handler.TypeSQLHandler;

/// Bean 映射计划
///
/// 本质上是对当前 ResultSetMetaData 和 TypeSQLHandler 做一次预处理,
/// 避免在逐行构建 Bean 时重复计算列名和 findHandler.
///
/// @author scx567888
/// @version 0.0.1
public record BeanMappingPlan(
    int[] fieldColumnIndexes,
    TypeSQLHandler<?>[] handlers
) {}
