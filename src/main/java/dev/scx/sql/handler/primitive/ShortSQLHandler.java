package dev.scx.sql.handler.primitive;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ShortSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class ShortSQLHandler implements TypeSQLHandler<Short> {

    private final boolean isPrimitive;

    public ShortSQLHandler(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return this.isPrimitive ? typeOf(short.class) : typeOf(Short.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, Short value) throws SQLException {
        ps.setShort(i, value);
    }

    @Override
    public Short read(ResultSet rs, int i) throws SQLException {
        var value = rs.getShort(i);
        if (rs.wasNull()) {
            return missingValue();
        }
        return value;
    }

    @Override
    public Short missingValue() {
        return isPrimitive ? (short) 0 : null;
    }

}
