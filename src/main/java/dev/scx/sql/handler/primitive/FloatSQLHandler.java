package dev.scx.sql.handler.primitive;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// FloatSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class FloatSQLHandler implements TypeSQLHandler<Float> {

    private final boolean isPrimitive;

    public FloatSQLHandler(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return this.isPrimitive ? typeOf(float.class) : typeOf(Float.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, Float value) throws SQLException {
        ps.setFloat(i, value);
    }

    @Override
    public Float read(ResultSet rs, int i) throws SQLException {
        var value = rs.getFloat(i);
        if (rs.wasNull()) {
            return missingValue();
        }
        return value;
    }

    @Override
    public Float missingValue() {
        return isPrimitive ? 0f : null;
    }

}
