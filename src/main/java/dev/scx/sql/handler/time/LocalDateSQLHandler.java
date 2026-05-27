package dev.scx.sql.handler.time;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.*;
import java.time.LocalDate;

import static dev.scx.reflect.ScxReflect.typeOf;

/// 为不支持 LocalDate 的数据库添加 降级支持
///
/// @author scx567888
/// @version 0.0.1
public final class LocalDateSQLHandler implements TypeSQLHandler<LocalDate> {

    @Override
    public TypeInfo valueType() {
        return typeOf(LocalDate.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, LocalDate value) throws SQLException {
        try {
            ps.setObject(i, value);
        } catch (SQLFeatureNotSupportedException e) {
            ps.setDate(i, Date.valueOf(value));
        }
    }

    @Override
    public LocalDate read(ResultSet rs, int i) throws SQLException {
        try {
            return rs.getObject(i, LocalDate.class);
        } catch (SQLFeatureNotSupportedException e) {
            var value = rs.getDate(i);
            return value == null ? null : value.toLocalDate();
        }
    }

}
