package dev.scx.sql;

import java.util.List;

/// BatchSQL
///
/// @author scx567888
/// @version 0.0.1
public interface BatchSQL {

    static BatchSQL batchSQL(String sql, List<Object[]> batchParams) {
        return new DefaultBatchSQL(sql, batchParams);
    }

    /// SQL 语句 (问号形式 '?' 占位)
    String sql();

    List<Object[]> batchParams();

}
