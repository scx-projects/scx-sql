package dev.scx.sql.extractor.map;

/// Map 映射计划
///
/// 本质上是对当前 ResultSetMetaData 做一次预处理,
/// 避免在逐行构建 Map 时重复执行 mapKeyMapping.
///
/// @author scx567888
/// @version 0.0.1
public record MapMappingPlan(
    String[] mapKeys
) {}
