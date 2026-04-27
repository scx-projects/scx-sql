package dev.scx.sql.handler.primitive;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// DoubleSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class DoubleSQLHandler implements TypeSQLHandler<Double> {

    private final boolean isPrimitive;

    public DoubleSQLHandler(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeInfo valueType() {
        return this.isPrimitive ? typeOf(double.class) : typeOf(Double.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, Double value) throws SQLException {
        ps.setDouble(i, value);
    }

    @Override
    public Double read(ResultSet rs, int i) throws SQLException {
        var value = rs.getDouble(i);
        if (rs.wasNull()) {
            return missingValue();
        }
        return value;
    }

    @Override
    public Double missingValue() {
        return isPrimitive ? 0.0 : null;
    }

}
