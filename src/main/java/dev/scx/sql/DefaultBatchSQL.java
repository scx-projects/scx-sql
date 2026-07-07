package dev.scx.sql;

import java.util.List;

/// 默认 BatchSQL 实现
///
/// @author scx567888
public record DefaultBatchSQL(String sql, List<Object[]> batchParams) implements BatchSQL {

}
