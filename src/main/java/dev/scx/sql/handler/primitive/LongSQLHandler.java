package dev.scx.sql.handler.primitive;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// LongSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class LongSQLHandler implements TypeSQLHandler<Long> {

    private final boolean isPrimitive;

    public LongSQLHandler(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return this.isPrimitive ? typeOf(long.class) : typeOf(Long.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, Long value) throws SQLException {
        ps.setLong(i, value);
    }

    @Override
    public Long read(ResultSet rs, int i) throws SQLException {
        var value = rs.getLong(i);
        if (rs.wasNull()) {
            return missingValue();
        }
        return value;
    }

    @Override
    public Long missingValue() {
        return isPrimitive ? 0L : null;
    }

}
