package dev.scx.sql.handler.time;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static dev.scx.reflect.ScxReflect.typeOf;

/// OffsetDateTime 无法降级支持
///
/// @author scx567888
/// @version 0.0.1
public final class OffsetDateTimeSQLHandler implements TypeSQLHandler<OffsetDateTime> {

    @Override
    public TypeInfo valueType() {
        return typeOf(OffsetDateTime.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, OffsetDateTime value) throws SQLException {
        ps.setObject(i, value);
    }

    @Override
    public OffsetDateTime read(ResultSet rs, int i) throws SQLException {
        return rs.getObject(i, OffsetDateTime.class);
    }

}
