package dev.scx.sql.handler.primitive;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BooleanSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class BooleanSQLHandler implements TypeSQLHandler<Boolean> {

    private final boolean isPrimitive;

    public BooleanSQLHandler(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return this.isPrimitive ? typeOf(boolean.class) : typeOf(Boolean.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, Boolean value) throws SQLException {
        ps.setBoolean(i, value);
    }

    @Override
    public Boolean read(ResultSet rs, int i) throws SQLException {
        var value = rs.getBoolean(i);
        if (rs.wasNull()) {
            return missingValue();
        }
        return value;
    }

    @Override
    public Boolean missingValue() {
        return isPrimitive ? false : null;
    }

}
