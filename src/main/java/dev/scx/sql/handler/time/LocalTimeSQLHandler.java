package dev.scx.sql.handler.time;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.*;
import java.time.LocalTime;

import static dev.scx.reflect.ScxReflect.typeOf;

/// 为不支持 LocalTime 的数据库添加 降级支持
///
/// 注意: fallback 到 java.sql.Time 时仅保留时/分/秒, 纳秒精度会丢失.
///
/// @author scx567888
/// @version 0.0.1
public final class LocalTimeSQLHandler implements TypeSQLHandler<LocalTime> {

    @Override
    public TypeInfo valueType() {
        return typeOf(LocalTime.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, LocalTime value) throws SQLException {
        try {
            ps.setObject(i, value);
        } catch (SQLFeatureNotSupportedException e) {
            ps.setTime(i, Time.valueOf(value));
        }
    }

    @Override
    public LocalTime read(ResultSet rs, int i) throws SQLException {
        try {
            return rs.getObject(i, LocalTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var value = rs.getTime(i);
            return value == null ? null : value.toLocalTime();
        }
    }

}
