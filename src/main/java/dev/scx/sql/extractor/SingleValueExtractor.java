package dev.scx.sql.extractor;

import dev.scx.sql.TypeSQLResolver;

import java.sql.ResultSet;
import java.sql.SQLException;

/// SingleValueExtractor
///
/// @param <T> t
/// @author scx567888
/// @version 0.0.1
public final class SingleValueExtractor<T> implements ResultSetExtractor<T, RuntimeException> {

    /// 列索引 (为 null 表示使用 columnLabel).
    private final Integer columnIndex;

    /// 列标签
    private final String columnLabel;

    /// 所需 class
    private final Class<T> type;

    public SingleValueExtractor(Class<T> type) {
        this(1, type);
    }

    public SingleValueExtractor(int columnIndex, Class<T> type) {
        this(columnIndex, null, type);
    }

    public SingleValueExtractor(String columnLabel, Class<T> type) {
        if (columnLabel == null) {
            throw new NullPointerException("columnLabel can not be null");
        }
        this(null, columnLabel, type);
    }

    private SingleValueExtractor(Integer columnIndex, String columnLabel, Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type can not be null");
        }
        this.columnIndex = columnIndex;
        this.columnLabel = columnLabel;
        this.type = type;
    }

    @Override
    public T extract(ResultSet rs, TypeSQLResolver resolver) throws SQLException {
        if (rs.next()) {
            var index = columnIndex == null ? rs.findColumn(columnLabel) : columnIndex;
            return resolver.read(rs, index, type);
        } else {
            // 没有 row 我们抛异常.
            throw new SQLException("Expected one row, but got none");
        }
    }

}
