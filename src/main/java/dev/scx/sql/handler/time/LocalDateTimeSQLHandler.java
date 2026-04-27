package dev.scx.sql.handler.time;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.*;
import java.time.LocalDateTime;

import static dev.scx.reflect.ScxReflect.typeOf;

/// 为不支持 LocalDateTime 的数据库添加 降级支持
///
/// @author scx567888
/// @version 0.0.1
public final class LocalDateTimeSQLHandler implements TypeSQLHandler<LocalDateTime> {

    @Override
    public TypeInfo valueType() {
        return typeOf(LocalDateTime.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, LocalDateTime value) throws SQLException {
        try {
            ps.setObject(i, value);
        } catch (SQLFeatureNotSupportedException e) {
            ps.setTimestamp(i, Timestamp.valueOf(value));
        }
    }

    @Override
    public LocalDateTime read(ResultSet rs, int i) throws SQLException {
        try {
            return rs.getObject(i, LocalDateTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var value = rs.getTimestamp(i);
            return value == null ? null : value.toLocalDateTime();
        }
    }

}
