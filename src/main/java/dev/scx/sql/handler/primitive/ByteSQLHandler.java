package dev.scx.sql.handler.primitive;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ByteSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class ByteSQLHandler implements TypeSQLHandler<Byte> {

    private final boolean isPrimitive;

    public ByteSQLHandler(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return this.isPrimitive ? typeOf(byte.class) : typeOf(Byte.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, Byte value) throws SQLException {
        ps.setByte(i, value);
    }

    @Override
    public Byte read(ResultSet rs, int i) throws SQLException {
        var value = rs.getByte(i);
        if (rs.wasNull()) {
            return missingValue();
        }
        return value;
    }

    @Override
    public Byte missingValue() {
        return isPrimitive ? (byte) 0 : null;
    }

}
