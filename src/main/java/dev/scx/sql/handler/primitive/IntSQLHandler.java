package dev.scx.sql.handler.primitive;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// IntSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class IntSQLHandler implements TypeSQLHandler<Integer> {

    private final boolean isPrimitive;

    public IntSQLHandler(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return this.isPrimitive ? typeOf(int.class) : typeOf(Integer.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, Integer value) throws SQLException {
        ps.setInt(i, value);
    }

    @Override
    public Integer read(ResultSet rs, int i) throws SQLException {
        var value = rs.getInt(i);
        if (rs.wasNull()) {
            return missingValue();
        }
        return value;
    }

    @Override
    public Integer missingValue() {
        return isPrimitive ? 0 : null;
    }

}
