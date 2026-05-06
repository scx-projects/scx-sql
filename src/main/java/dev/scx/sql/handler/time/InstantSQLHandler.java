package dev.scx.sql.handler.time;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.*;
import java.time.Instant;

import static dev.scx.reflect.ScxReflect.typeOf;

/// 为不支持 Instant 的数据库添加 降级支持
///
/// @author scx567888
/// @version 0.0.1
public final class InstantSQLHandler implements TypeSQLHandler<Instant> {

    @Override
    public TypeInfo valueType() {
        return typeOf(Instant.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, Instant value) throws SQLException {
        try {
            ps.setObject(i, value);
        } catch (SQLFeatureNotSupportedException e) {
            ps.setTimestamp(i, Timestamp.from(value));
        }
    }

    @Override
    public Instant read(ResultSet rs, int i) throws SQLException {
        try {
            return rs.getObject(i, Instant.class);
        } catch (SQLFeatureNotSupportedException e) {
            var value = rs.getTimestamp(i);
            return value == null ? null : value.toInstant();
        }
    }

}
