package dev.scx.sql.handler.time;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetTime;

import static dev.scx.reflect.ScxReflect.typeOf;

/// OffsetTime 无法降级支持
///
/// @author scx567888
/// @version 0.0.1
public final class OffsetTimeSQLHandler implements TypeSQLHandler<OffsetTime> {

    @Override
    public TypeInfo valueType() {
        return typeOf(OffsetTime.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, OffsetTime value) throws SQLException {
        ps.setObject(i, value);
    }

    @Override
    public OffsetTime read(ResultSet rs, int i) throws SQLException {
        return rs.getObject(i, OffsetTime.class);
    }

}
